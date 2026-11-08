package dev.tdwalsh.project.tabletopBeholder.templateApi;

import dev.tdwalsh.project.tabletopBeholder.exceptions.CurlException;
import dev.tdwalsh.project.tabletopBeholder.exceptions.MalformedInputException;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpTimeoutException;
import java.nio.ByteBuffer;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Flow;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import javax.inject.Singleton;

@Singleton
public class Open5EClient {
    private static final Duration REQUEST_TIMEOUT = Duration.ofSeconds(5);
    private static final int MAX_RESPONSE_BYTES = 2 * 1024 * 1024;

    private final HttpClient client;

    public Open5EClient() {
        this(HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(2))
                .followRedirects(HttpClient.Redirect.NEVER)
                .build());
    }

    Open5EClient(HttpClient client) {
        this.client = client;
    }

    /**
     * Fetches an Open5e resource within one deadline, including the response body.
     * @param uri Open5e URL
     * @return the upstream response
     */
    public HttpResponse<String> call(String uri) {
        URI target;
        try {
            target = URI.create(uri);
        } catch (IllegalArgumentException exception) {
            throw new MalformedInputException("Invalid Open5e request URL.", exception);
        }
        requireOpen5e(target);
        long deadline = System.nanoTime() + REQUEST_TIMEOUT.toNanos();
        HttpResponse<String> response = fetch(target, deadline);
        if (isRedirect(response.statusCode())) {
            String location = response.headers().firstValue("Location")
                    .orElseThrow(() -> new CurlException("Open5e sent a redirect without a location."));
            try {
                target = target.resolve(location);
                requireOpen5e(target);
            } catch (IllegalArgumentException | MalformedInputException exception) {
                throw new CurlException("Open5e sent an invalid redirect.", exception);
            }
            response = fetch(target, deadline);
        }
        return response;
    }

    private HttpResponse<String> fetch(URI target, long deadline) {
        long remaining = deadline - System.nanoTime();
        if (remaining <= 0) {
            throw timeout();
        }
        HttpRequest request = HttpRequest.newBuilder(target)
                .timeout(Duration.ofNanos(remaining))
                .header("Accept", "application/json")
                .GET()
                .build();

        LimitedBody body = new LimitedBody();
        CompletableFuture<HttpResponse<String>> response = client.sendAsync(request, ignored -> body);
        try {
            return response.get(Math.max(0, deadline - System.nanoTime()), TimeUnit.NANOSECONDS);
        } catch (TimeoutException exception) {
            body.cancel();
            response.cancel(true);
            throw timeout();
        } catch (InterruptedException exception) {
            body.cancel();
            response.cancel(true);
            Thread.currentThread().interrupt();
            throw new CurlException("Open5e request interrupted.", exception);
        } catch (ExecutionException exception) {
            throw new CurlException("Open5e request failed.", exception.getCause());
        }
    }

    private static CurlException timeout() {
        return new CurlException("Open5e request timed out.", new HttpTimeoutException("Open5e deadline exceeded."));
    }

    private static boolean isRedirect(int status) {
        return status == 301 || status == 302 || status == 303 || status == 307 || status == 308;
    }

    private static void requireOpen5e(URI uri) {
        if (!"https".equalsIgnoreCase(uri.getScheme()) || !"api.open5e.com".equalsIgnoreCase(uri.getHost())
                || uri.getUserInfo() != null || (uri.getPort() != -1 && uri.getPort() != 443)) {
            throw new MalformedInputException("Invalid Open5e request URL.");
        }
    }

    private static final class LimitedBody implements HttpResponse.BodySubscriber<String> {
        private final HttpResponse.BodySubscriber<String> delegate =
                HttpResponse.BodySubscribers.ofString(java.nio.charset.StandardCharsets.UTF_8);
        private Flow.Subscription subscription;
        private boolean cancelled;
        private long receivedBytes;

        @Override
        public CompletionStage<String> getBody() {
            return delegate.getBody();
        }

        @Override
        public synchronized void onSubscribe(Flow.Subscription incoming) {
            if (cancelled) {
                incoming.cancel();
                return;
            }
            subscription = incoming;
            delegate.onSubscribe(incoming);
        }

        @Override
        public synchronized void onNext(List<ByteBuffer> buffers) {
            if (cancelled) {
                return;
            }
            for (ByteBuffer buffer : buffers) {
                receivedBytes += buffer.remaining();
                if (receivedBytes > MAX_RESPONSE_BYTES) {
                    cancel();
                    return;
                }
            }
            delegate.onNext(buffers);
        }

        @Override
        public synchronized void onError(Throwable error) {
            delegate.onError(error);
        }

        @Override
        public synchronized void onComplete() {
            delegate.onComplete();
        }

        private synchronized void cancel() {
            if (!cancelled) {
                cancelled = true;
                if (subscription != null) {
                    subscription.cancel();
                }
                delegate.onError(new IOException("Open5e response cancelled or too large."));
            }
        }
    }
}
