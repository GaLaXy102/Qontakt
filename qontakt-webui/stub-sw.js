var version = 'v1::';

console.log("Q-UI: Loaded stub service worker to enable PWA.");
// from https://css-tricks.com/serviceworker-for-offline/
self.addEventListener("install", function(event) {
    console.log('WORKER: install event in progress.');
    event.waitUntil(
        /* The caches built-in is a promise-based API that helps you cache responses,
           as well as finding and deleting them.
        */
        caches
            /* You can open a cache by name, and this method returns a promise. We use
               a versioned cache name here so that we can remove old cache entries in
               one fell swoop later, when phasing out an older service worker.
            */
            .open(version + 'fundamentals')
            .then(function(cache) {
                /* After the cache is opened, we can fill it with the offline fundamentals.
                   The method below will add all resources we've indicated to the cache,
                   after making HTTP requests for each of them.
                */
                return cache.addAll([
                    '/offline.html',
                    '/css/bootstrap.min.css',
                    '/css/bootstrap-grid.min.css',
                    '/css/bootstrap-reboot.min.css',
                    '/css/w3.css',
                    '/css/qontakt.css',
                    '/js/bootstrap.min.js',
                    '/js/jquery-3.5.1.min.js',
                    '/js/popper.min.js',
                    '/js/qontakt.js',
                    '/js/qrcode.min.js',
                    '/js/qr-scanner.umd.min.js',
                    '/js/qr-scanner-worker.min.js',
                    '/auth/js/qontakt-auth.js',
                    '/img/icon-small.png',
                    '/img/logo-small.png',
                ]);
            })
            .then(function() {
                console.log('WORKER: install completed');
            })
    );
});

// from https://paul.kinlan.me/de/offline-fallback-page-with-service-worker/
self.addEventListener('fetch', (event) => {
    const { request } = event;

    // Always bypass for range requests, due to browser bugs
    if (request.headers.has('range')) return;
    event.respondWith(async function() {
        // Try to get from the cache:
        const cachedResponse = await caches.match(request);
        if (cachedResponse) return cachedResponse;

        try {
            // See https://developers.google.com/web/updates/2017/02/navigation-preload#using_the_preloaded_response
            const response = await event.preloadResponse;
            if (response) return response;

            // Otherwise, get from the network
            return await fetch(request);
        } catch (err) {
            // If this was a navigation, show the offline page:
            if (request.mode === 'navigate') {
                return caches.match('/offline.html');
            }

            // Otherwise throw
            throw err;
        }
    }());
});