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
        "confirmVisit": "Besuch bestätigen",
        "dismiss": "Abbrechen",
        "save": "Speichern",
        "confirmVisitHeader": "Bitte bestätigen Sie den Besuch in folgendem Lokal:",
        "confirmCheckout": "Besuch beenden",
        "confirmCheckoutText": "Möchten Sie Ihren Besuch wirklich beenden?",
        "valid": "Gültig",
        "yes": "ja",
        "no": "nein",
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
    "lb-q-savevisit": "confirmVisit",
    "btn-q-dismiss": "dismiss",
    "btn-q-savevisit": "save",
    "btn-q-closevisit": "save",
    "lb-q-closevisit": "confirmCheckout",
    "lb-q-closevisit-text": "confirmCheckoutText",
    "lb-q-valid": "valid",
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

function formatLokalForCheckin(lokalData) {
    return getTranslation("confirmVisitHeader") + "\n" + lokalData.name + "\n" + lokalData.address + "\n" + lokalData.gdprContact;
}

// QR INPUT

const qrEnabled = ['checkin', 'verify'];

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

const QRLensAltlisteners = new Map(Object.entries({
    "checkin": listenInputGetLokal,
    "verify": listenInputVerify,
}));

function initializeQR(pageName) {
    qontaktQRScanner.resetInstance();
    if (qrEnabled.includes(pageName)) {
        if (QRLensAltlisteners.has(pageName)) {
            document.getElementById('q-qr-lens-alt').addEventListener('input', QRLensAltlisteners.get(pageName));
        }
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
    document.getElementById('q-qr-lens-alt-wrapper').classList.remove('d-none');
    document.getElementById('q-qr-lens-wrapper').remove();
}

function toggleFlash() {
    qontaktQRScanner.getInstance().hasFlash().then(function (flashAvail) {
        if (flashAvail) qontaktQRScanner.getInstance().toggleFlash();
    })
}

function listenInputGetLokal(ev) {
    const btn = document.getElementById('btn-q-checkinto');
    const elem = ev.target;
    const text = elem.value.toString();
    if (!text.match(uidPattern)) {
        elem.classList.add('bg-warning');
        elem.classList.remove('bg-success', 'bg-failure');
        btn.setAttribute('disabled', 'true');
    } else {
        elem.classList.remove('bg-warning');
        const result = queryLokalData(text);
        if (result[0]) {
            elem.classList.remove('bg-failure');
            elem.classList.add('bg-success');
            document.getElementById('q-qr-lens-friendly').value = result[1].name;
            btn.removeAttribute('disabled');
            document.getElementById('lb-q-savevisit-text').innerText = formatLokalForCheckin(result[1]);
        } else {
            elem.classList.remove('bg-success');
            elem.classList.add('bg-failure');
            document.getElementById('q-qr-lens-friendly').value = "";
            btn.setAttribute('disabled', 'true');
        }
    }
}

const uidPattern = "[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[34][0-9a-fA-F]{3}-[89ab][0-9a-fA-F]{3}-[0-9a-fA-F]{12}";
const longPattern = "[0-9]{1,19}";
const verifyRegex = "///" + uidPattern + "/" + uidPattern + "//" + longPattern + "///";

function listenInputVerify(ev) {
    const elem = ev.target;
    const text = elem.value.toString();
    const friendly = document.getElementById('q-qr-lens-friendly');
    if (!text.match(verifyRegex)) {
        // Warning in Input field for not matching regex
        elem.classList.add('bg-warning');
        // Timeout to allow batch processing
        window.setTimeout(() => friendly.classList.remove('bg-warning'), 1000);
        // Warning in Output field for bad value read
        friendly.classList.add('bg-warning');
        friendly.classList.remove('bg-success', 'bg-failure');
        friendly.value = getTranslation('no');
    } else {
        // Remove any warnings (conservatively to avoid blinking)
        elem.classList.remove('bg-warning');
        friendly.classList.remove('bg-warning');
        // Query verfication result
        const result = queryVerification(text);
        if (result) {
            // Set output color
            friendly.classList.remove('bg-failure');
            friendly.classList.add('bg-success');
            // Timeout to allow batch processing
            window.setTimeout(() => friendly.classList.remove('bg-success'), 1000);
            friendly.value = getTranslation('yes');
        } else {
            // Set output color
            elem.classList.remove('bg-success');
            elem.classList.add('bg-failure');
            friendly.classList.remove('bg-success');
            friendly.classList.add('bg-failure');
            // Timeout to allow batch processing
            window.setTimeout(() => friendly.classList.remove('bg-failure'), 1000);
            friendly.value = getTranslation('no');
        }
    }
}

// AUTOSTART

window.onload = function () {
    preferredLang = getLanguagePreference();
    redirectForbidden(getPageName());
    setButtonStates(hasActiveVisit(), getPageName());
    setTranslations();
    initializeQR(getPageName());
}

// ROUTING
const forbiddenActive = ['checkin'];
const forbiddenInactive = ['myvisit'];
function redirectForbidden(pageName) {
    if (hasActiveVisit()) {
        if (forbiddenActive.includes(pageName)) document.location = 'index.html';
    } else {
        if (forbiddenInactive.includes(pageName)) document.location = 'index.html';
    }
}

const nextAction = new Map(Object.entries({
    "btn-q-checkin": function () {
        window.location.href = "checkin.html";
    },
    "btn-q-closevisit": function () {
        const response = performCheckout();
        if (response[0]) {
            window.location.reload(false);
        } else {
            window.alert(getTranslation('error') + ": " + response[1]);
        }
    },
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
    "btn-q-savevisit": function () {
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

function queryLokalData(uuid) {
    // TODO implement me
    console.log("Method not implemented: queryLokalData")
    const sampleData = {
        "name": "Zur Fröhlichen Reblaus",
        "address": "Weinstraße 3, 01069 Dresden",
        "coordinates": {
            "x": 0,
            "y": 0
        },
        "owner": "UID-of-Owner",
        "gdprContact": "gdpr@qontakt.me",
        "checkoutTime": "12:34:56",
        "federalState": {
            "countryCode": "DEU",
            "shortName": "SN"
        }
    };
    return [true, sampleData];
}

function hasActiveVisit() {
    // TODO implement me
    console.log("Method not implemented: hasActiveVisit");
    return window.localStorage.getItem('stubVisitActive') === "true";
}

function performCheckin() {
    // TODO implement me
    console.log("Method not implemented: performCheckin");
    window.localStorage.setItem('stubVisitActive', true);
    return [true, 200];
}

function performCheckout() {
    // TODO implement me
    console.log("Method not implemented: performCheckout");
    window.localStorage.setItem('stubVisitActive', false);
    return [true, 200];
}

function queryVerification(veriString) {
    // TODO implement me
    console.log("Method not implemented: queryVerification");
    return true;
}
