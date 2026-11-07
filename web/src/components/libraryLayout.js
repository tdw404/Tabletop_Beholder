const compact = () => window.matchMedia('(max-width: 700px)').matches;
let listScroll = 0;

export function bindLibraryLayout(tableId, select) {
    const table = document.getElementById(tableId);
    table.addEventListener('click', event => {
        const row = event.target.closest('tbody tr[data-id]');
        if (row) select(row.dataset.id);
    });
    document.querySelector('.mc-list-pane form')?.addEventListener('submit', event => {
        event.preventDefault();
        document.querySelector('.mc-list-pane #filter-btn, .mc-list-pane #filter-session-btn')?.click();
    });
    document.getElementById('mc-library-back').addEventListener('click', () => {
        document.querySelector('.mc-library').classList.remove('mc-detail-open');
        document.querySelector('.mc-record-list .selectedRow button')?.focus({preventScroll: true});
        window.scrollTo(0, listScroll);
    });
}

export function showLibraryDetail() {
    const layout = document.querySelector('.mc-library');
    if (layout.classList.contains('mc-detail-open')) return;
    listScroll = window.scrollY;
    layout.classList.add('mc-detail-open');
    if (compact()) {
        document.getElementById('mc-library-back').focus({preventScroll: true});
        window.scrollTo(0, 0);
    }
}

export function renderLibraryRow(cell, name, metadata) {
    const button = document.createElement('button');
    button.className = 'mc-record-choice';
    button.type = 'button';
    const title = document.createElement('strong');
    title.textContent = name;
    const detail = document.createElement('small');
    detail.textContent = metadata.filter(Boolean).join(' · ');
    button.append(title, detail);
    cell.replaceChildren(button);
}
