import axios from "axios";

const INTERVAL_MS = 5 * 60 * 1000;
const STORAGE_KEY = "beholder:lastWarmupAttempt";
const ACTIVITY_EVENTS = ["pointerdown", "keydown", "visibilitychange"];

let stop;

export function startSessionWarmup(getToken) {
    if (stop) return;

    let lastActivity = Date.now();
    let lastAttempt = 0;
    let stopped = false;
    try {
        lastAttempt = Number(sessionStorage.getItem(STORAGE_KEY)) || 0;
    } catch {
        // The server cooldown still applies if browser storage is unavailable.
    }

    async function warmIfActive() {
        const now = Date.now();
        if (stopped || document.hidden || now - lastActivity >= INTERVAL_MS
                || now - lastAttempt < INTERVAL_MS) return;

        lastAttempt = now;
        try {
            sessionStorage.setItem(STORAGE_KEY, String(now));
        } catch {
            // Keep the cooldown in memory for this page.
        }
        try {
            const token = await getToken();
            if (!stopped && !document.hidden) {
                await axios.post("warmup", null, {
                    baseURL: process.env.API_BASE_URL,
                    headers: { Authorization: `Bearer ${token}` },
                    timeout: 25000,
                });
            }
        } catch (error) {
            if (error.response?.status === 401) stopSessionWarmup();
        }
    }

    function recordActivity() {
        if (document.hidden) return;
        lastActivity = Date.now();
        void warmIfActive();
    }

    for (const event of ACTIVITY_EVENTS) document.addEventListener(event, recordActivity);
    const timer = window.setInterval(warmIfActive, 30000);
    stop = () => {
        stopped = true;
        window.clearInterval(timer);
        for (const event of ACTIVITY_EVENTS) document.removeEventListener(event, recordActivity);
    };
    void warmIfActive();
}

export function stopSessionWarmup() {
    stop?.();
    stop = undefined;
    try {
        sessionStorage.removeItem(STORAGE_KEY);
    } catch {
        // Storage may be disabled.
    }
}
