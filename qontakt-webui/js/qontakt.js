// TRANSLATIONS

const translations = new Map(Object.entries({
    "de": {
        "checkIn": "Einchecken",
        "checkOut": "Auschecken",
        "myVisit": "Mein Besuch",
        "myData": "Meine Daten",
        "logout": "Abmelden",
        "verify": "Verifizieren",
        "back": "Zurück",
        "checkInTo": "Besuchen",
        "noCamera": "Leider konnte keine Kamera gefunden werden. Bitte geben Sie die ID unterhalb des QR-Codes ein.",
        "disabledCamera": "Bitte geben Sie den Zugriff auf Ihre Kamera frei oder geben Sie die ID unterhalb des" +
            " QR-Codes ein.",
        "error": "Fehler",
    }
}));

const defaultLang = "de";

let preferredLang;

const translatableFields = new Map(Object.entries({
    "btn-q-checkin": "checkIn",
    "btn-q-checkout": "checkOut",
    "btn-q-visitdetail": "myVisit",
    "btn-q-datadetail": "myData",
    "btn-q-logout": "logout",
    "btn-q-verify": "verify",
    "btn-q-back": "back",
    "btn-q-checkinto": "checkInTo",
}))

function getTranslation(name) {
    let text = translations.get(preferredLang)[name];
    if (text == null) {
        console.log("Q-UI: Missing translation for " + name + " in " + preferredLang);
        text = translations.get(defaultLang)[name];
    }
    return text;
}

function setTranslations() {
    translatableFields.forEach((trl, identifier) => {
        const elem = document.getElementById(identifier);
        if (elem != null) {
            elem.innerText = getTranslation(trl);
        }
    });
}

function getLanguagePreference() {
    const languagesAvailable = Array.from(navigator.languages.entries());
    for (let i = 0; i < languagesAvailable.length; ++i) {
        const currLang = languagesAvailable[i][1];
        if (translations.has(currLang)) {
            console.log("Q-UI: Using supported language " + currLang);
            return currLang;
        }
    }
    console.log("Q-UI: Using default language " + defaultLang);
    return defaultLang;
}

// PAGE PROPERTIES

function getPageName() {
    return document.getElementById('q-content').getAttribute("data-q-nav");
}

function setButtonStates(hasVisit, pageName) {
    const checkinButton = document.getElementById('btn-q-checkin');
    const checkoutButton = document.getElementById('btn-q-checkout');
    const myVisitButton = document.getElementById('btn-q-visitdetail');
    switch (pageName) {
        case "main":
            if (!hasVisit) {
                checkoutButton.remove();
                myVisitButton.setAttribute("disabled", "true");
            } else {
                checkinButton.remove();
            }
            break;
    }
}

// QR INPUT

const qrEnabled = new Array('checkin');

const qontaktQRScanner = (function () {
    let scanner;

    function createInstance() {
        const scanElem = document.getElementById('q-qr-lens');
        QrScanner.WORKER_PATH = "js/qr-scanner-worker.min.js";
        return new QrScanner(scanElem, result => {
            const elem = document.getElementById('q-qr-lens-alt');
            elem.value = result;
            elem.dispatchEvent(new Event('input'));
        });
    }

    return {
        getInstance: function () {
            if (!scanner) {
                scanner = createInstance();
            }
            return scanner;
        },
        resetInstance: function () {
            if (scanner) {
                scanner.destroy();
            }
            scanner = null;
        }
    }
})();

function initializeQR(pageName) {
    qontaktQRScanner.resetInstance();
    if (qrEnabled.includes(pageName)) {
        document.getElementById('q-qr-lens-alt').addEventListener('input', listenInputGetLokal);
        QrScanner.hasCamera().then(function (camAvail) {
            if (camAvail) {
                onCamera();
                qontaktQRScanner.getInstance().start()
                    .catch(function () {
                        onNoCamera();
                        window.alert(getTranslation("disabledCamera"));
                    });
            } else {
                onNoCamera();
                window.alert(getTranslation("noCamera"));
            }
        });
    }
}

function onCamera() {
    document.getElementById('q-qr-lens-alt-wrapper').classList.add('d-none');
}

function onNoCamera() {
    document.getElementById('q-qr-lens-alt').removeAttribute('disabled');
    document.getElementById('q-qr-lens-wrapper').remove();
}

function toggleFlash() {
    qontaktQRScanner.getInstance().hasFlash().then(function (flashAvail) {
        if (flashAvail) qontaktQRScanner.getInstance().toggleFlash();
    })
}

const UUID_LENGTH = 36;

function listenInputGetLokal(ev) {
    const btn = document.getElementById('btn-q-checkinto');
    const elem = ev.target;
    const text = elem.value.toString();
    if (text.length < UUID_LENGTH) {
        elem.classList.add('bg-warning');
        elem.classList.remove('bg-success', 'bg-failure');
        btn.setAttribute('disabled', 'true');
    } else {
        elem.classList.remove('bg-warning');
        const result = queryLokalName(text);
        if (result[0]) {
            elem.classList.remove('bg-failure');
            elem.classList.add('bg-success');
            document.getElementById('q-qr-lens-friendly').value = result[1];
            btn.removeAttribute('disabled');
        } else {
            elem.classList.remove('bg-success');
            elem.classList.add('bg-failure');
            document.getElementById('q-qr-lens-friendly').value = result[1];
            btn.setAttribute('disabled', 'true');
        }
    }
}

// AUTOSTART

window.onload = function () {
    preferredLang = getLanguagePreference();
    setButtonStates(hasActiveVisit(), getPageName());
    setTranslations();
    initializeQR(getPageName());
}

// ROUTING

const nextAction = new Map(Object.entries({
    "btn-q-checkin": function () {
        window.location.href = "checkin.html";
    },
    //"btn-q-checkout": function () { console.log("Implement me!") }, // TODO
    "btn-q-visitdetail": function () {
        window.location.href = "myvisit.html";
    },
    "btn-q-datadetail": function () {
        window.location.href = "mydata.html";
    },
    //"btn-q-logout": function () { console.log("Implement me!") }, // TODO
    "btn-q-verify": function () {
        window.location.href = "verify.html";
    },
    "btn-q-back": function () {
        history.go(-1);
    },
    "btn-q-checkinto": function () {
        const response = performCheckin();
        if (response[0]) {
            history.go(-1);
        } else {
            window.alert(getTranslation('error') + ": " + response[1]);
        }
    },
}))

function showNext(item) {
    try {
        nextAction.get(item.id)();
    } catch (e) {
        console.log("Q-UI: No action defined for " + item.id)
    }

}

// COMMUNICATION

function queryLokalName(uuid) {
    // TODO implement me
    return [true, "Zur fröhlichen Reblaus"];
}

function hasActiveVisit() {
    // TODO implement me
    return false;
}

function performCheckin() {
    // TODO implement me
    return [true, 200];
}
