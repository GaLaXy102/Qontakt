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
        "dismiss": "Schließen",
        "save": "Speichern",
        "confirmVisitHeader": "Bitte bestätigen Sie den Besuch in folgendem Lokal:",
        "confirmCheckout": "Besuch beenden",
        "confirmCheckoutText": "Möchten Sie Ihren Besuch wirklich beenden?",
        "valid": "Gültig",
        "yes": "ja",
        "no": "nein",
        "abort": "Abbrechen",
        "visitData": "Verifikationscode",
        "genericError": "Leider ist Qontakt derzeit nicht funktionsfähig. Bitte probieren Sie es in Kürze erneut.",
        "unauthorizedError": "Sie haben sich abgemeldet. Bitte melden Sie sich erneut an, bevor Sie diese Aktion" +
            " ausführen.",
        "forbiddenError": "Nein, das dürfen Sie nicht!",
        "hasVisitError": "Sie haben bereits einen Check-In.",
        "hasNoVisitError": "Sie haben keinen Check-In.",
        "hasCheckoutError": "Sie haben sich bereits ausgecheckt.",
        "invalidData": "Formulardaten ungültig",
        "lokal": "Lokal",
        "scanLokalFirst": "Bitte scannen Sie zuerst den QR-Code des Lokals, für welches Sie die Verifikation" +
            " vornehmen möchten.",
        "retry": "Erneut versuchen",
        "offlineLong": "Sie sind offline. Bitte überprüfen Sie Ihre Internetverbindung, um Qontakt zu benutzen.",
        "profileCreate": "Sie sind zum ersten Mal hier. Bitte erstellen Sie Ihr Profil.",
        "firstName": "Vorname",
        "lastName": "Nachname",
        "homeAddress": "Straße und Hausnummer",
        "homeZip": "PLZ",
        "homeCity": "Stadt",
        "telephoneNumber": "Telefonnummer",
        "allVisits": "Alle Besuche",
        "noVisit": "Es liegen keine Besuche vor.",
        "myLokals": "Meine Lokale",
        "noLokal": "Sie administrieren keine Lokale.",
        "add": "Hinzufügen",
        "addLokal": "Ein neues Lokal registrieren",
        "lokalName": "Name des Lokals",
        "address": "Adresse (mit PLZ und Ort)",
        "checkoutTime": "Zeit für Check-Out",
        "checkoutTimeLong": "Um diese Uhrzeit werden automatisch alle Besuche geschlossen.",
        "country": "Staat",
        "state": "Bundesland",
        "password": "Ihr Passwort",
        "passwordLong": "Bitte notieren Sie sich das folgende Passwort:\n/pwd/\nSie benötigen es, um Daten über" +
            " Besuche in Ihrem Lokal abrufen zu können. Es kann nicht wiederhergestellt werden! Verwahren Sie es" +
            " deshalb sicher.",
        "exportData": "Daten exportieren",
        "encryptAndExport": "Verschlüsseln und Herunterladen",
        "lokalToExport": "Lokal für Datenexport",
        "target": "Empfänger",
        "wrongPassword": "Falsches Passwort!",
        "loc-DEU": "Deutschland",
        "loc-DEU-BW": "Baden-Württemberg",
        "loc-DEU-BY": "Bayern",
        "loc-DEU-BE": "Berlin",
        "loc-DEU-BB": "Brandenburg",
        "loc-DEU-HB": "Bremen",
        "loc-DEU-HH": "Hamburg",
        "loc-DEU-HE": "Hessen",
        "loc-DEU-MV": "Mecklenburg-Vorpommern",
        "loc-DEU-NI": "Niedersachsen",
        "loc-DEU-NW": "Nordrhein-Westfalen",
        "loc-DEU-RP": "Rheinland-Pfalz",
        "loc-DEU-SL": "Saarland",
        "loc-DEU-SN": "Sachsen",
        "loc-DEU-ST": "Sachsen-Anhalt",
        "loc-DEU-SH": "Schleswig-Holstein",
        "loc-DEU-TH": "Thüringen",
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
    "btn-q-dismiss": "abort",
    "btn-q-dismiss2": "dismiss",
    "btn-q-dismiss3": "dismiss",
    "btn-q-savevisit": "save",
    "btn-q-closevisit": "save",
    "lb-q-closevisit": "confirmCheckout",
    "lb-q-closevisit-text": "confirmCheckoutText",
    "lb-q-valid": "valid",
    "lb-q-visit-data": 'visitData',
    "lb-q-lokal": "lokal",
    "btn-q-retry-offline": "retry",
    "lb-q-offline": "offlineLong",
    "lb-q-firstName": "firstName",
    "lb-q-lastName": "lastName",
    "lb-q-homeAddress": "homeAddress",
    "lb-q-homeZip": "homeZip",
    "lb-q-homeCity": "homeCity",
    "lb-q-telephoneNumber": "telephoneNumber",
    "btn-q-save-profile": "save",
    "btn-q-listvisits": "allVisits",
    "lb-q-novisit": "noVisit",
    "btn-q-listlokals": "myLokals",
    "lb-q-nolokal": "noLokal",
    "btn-q-addlokal": "add",
    "lb-q-addlokal": "addLokal",
    "btn-q-sendcreatelokal": "save",
    "lb-q-lokalname": "lokalName",
    "lb-q-lokaladdress": "address",
    "lb-q-lokalcheckout": "checkoutTime",
    "lb-q-lokalcheckout-help": "checkoutTimeLong",
    "lb-q-lokalcountry": "country",
    "lb-q-lokalstate": "state",
    "lb-q-password": "password",
    "btn-q-close": "dismiss",
    "btn-q-export": "exportData",
    "lb-q-export": "exportData",
    "btn-q-performexport": "encryptAndExport",
    "lb-q-lokalexport": "lokalToExport",
    "lb-q-passwordexport": "password",
    "lb-q-targetexport": "target",
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

function hasCreate() {
    return new URLSearchParams(location.search).get("create") !== null;
}

// OUTPUTS

function formatLokalForCheckin(lokalData) {
    return getTranslation("confirmVisitHeader") + "\n" + lokalData.name + "\n" + lokalData.address + "\n" + lokalData.gdprContact;
}

function setVerificationData() {
    getVerificationString(function (verification) {
        // Delete any Children
        document.getElementById('q-qr-output-img').innerHTML = "";
        new QRCode(document.getElementById('q-qr-output-img'), {
            text: verification,
            width: 600,
            height: 600,
            correctLevel: QRCode.CorrectLevel.L
        });
        document.getElementById('q-qr-output-text').innerText = verification;
        // Repeat every 25 seconds asynchronously
        window.setTimeout(setVerificationData, 25000);
    });
}

function addVisitToList(obj, lokals, wrapper) {
    const listElement = document.createElement("div");
    listElement.classList.add("list-group-item");
    if (!obj.checkOut) {
        listElement.classList.add("active");
    }
    const listElementHeader = document.createElement("div");
    listElementHeader.classList.add("d-flex", "w-100", "justify-content-between", "align-items-center");
    const listElementHeaderText = document.createElement("h5");
    listElementHeaderText.classList.add("mb-1");
    // Query lokal cache, on miss use data from query
    let lokalData = lokals.get(obj.lokalUid);
    if (!lokalData) {
        queryLokalData(obj.lokalUid, lokalData => {
            lokals.set(obj.lokalUid, lokalData);
            listElementHeaderText.innerText = lokalData.name;
        });
    } else {
        listElementHeaderText.innerText = lokalData.name;
    }
    listElementHeader.appendChild(listElementHeaderText);
    const listElementHeaderTime = document.createElement("small");
    listElementHeaderTime.innerText = timeago.format(obj.checkIn + 'Z', preferredLang);
    listElementHeader.appendChild(listElementHeaderTime);
    // Allow for subtext later, thus the extra div
    listElement.appendChild(listElementHeader);
    wrapper.appendChild(listElement);
}

function addLokalToList(obj, wrapper) {
    const listElement = document.createElement("div");
    listElement.classList.add("list-group-item");
    const listElementHeader = document.createElement("div");
    listElementHeader.classList.add("d-flex", "w-100", "justify-content-between", "align-items-center");
    const listElementHeaderText = document.createElement("h5");
    listElementHeaderText.classList.add("mb-1");
    listElementHeaderText.innerText = obj.name;
    listElementHeader.appendChild(listElementHeaderText);
    const listElementHeaderBadge = document.createElement("span");
    listElementHeaderBadge.classList.add("badge", "bagde-primary", "badge-pill");
    listElementHeaderBadge.setAttribute("onclick", "downloadMarketing(this)");
    listElementHeaderBadge.setAttribute("data-q-lokaluid", obj.lokalUid);
    const listElementHeaderBadgeIcon = document.createElement("i");
    listElementHeaderBadgeIcon.classList.add("bi", "bi-download");
    listElementHeaderBadge.appendChild(listElementHeaderBadgeIcon);
    listElementHeader.appendChild(listElementHeaderBadge);
    listElement.appendChild(listElementHeader);
    const listElementContent = document.createElement("small");
    listElementContent.innerText = obj.address;
    listElement.appendChild(listElementContent);
    wrapper.appendChild(listElement);
}

function setProfileData() {
    if (hasCreate()) {
        document.getElementById('btn-q-back').remove();
        setMsg("profileCreate", document.getElementById('q-form'));
    } else {
        const logoutBtn = document.getElementById('btn-q-logout');
        if (logoutBtn) {
            logoutBtn.remove();
        }
        $.get(qontaktIDMEndpoint + "?userUid=" + userUid).then(response => {
            $.each(response, (key, value) => {
                const insertElement = document.getElementById(key);
                if (insertElement) {
                    insertElement.value = value;
                }
            });
        });
    }
    // Kratos is always right.
    document.getElementById('userUid').value = userUid;
    $.get(kratosSessionEndpoint).then(response => {
        document.getElementById("email").value = response.identity.traits.email;
    });
}

let rulesAvailable;

function addRulesToList() {
    const countryWrapper = document.getElementById("lokalcountry");
    $.each(rulesAvailable, key => {
        const optionElement = document.createElement("option");
        optionElement.value = key;
        optionElement.innerText = getTranslation("loc-" + key);
        countryWrapper.appendChild(optionElement);
    });
}

function addLokalsToView(lokals) {
    const wrapper = document.getElementById("lokallist");
    $.each(lokals, (idx, obj) => {
        addLokalToList(obj, wrapper);
    });
}

function addLokalsToOptionList(lokals) {
    const wrapper = document.getElementById('lokalexport');
    $.each(lokals, (idx, item) => {
        const optionElement = document.createElement("option");
        optionElement.value = item.lokalUid;
        optionElement.innerText = item.name;
        wrapper.appendChild(optionElement);
    });
}

function addEncryptionTargetsToOptionList(targets) {
    const wrapper = document.getElementById('targetexport');
    $.each(targets, (idx, item) => {
        const optionElement = document.createElement("option");
        optionElement.value = item.id;
        optionElement.innerText = item.name;
        wrapper.appendChild(optionElement);
    })
}

function showAllLokals(lokals) {
    if (lokals.length) {
        // Clear view
        document.getElementById('lb-q-nolokal').classList.add("hidden");
        document.getElementById('btn-q-export').classList.remove("hidden");
    }
    addLokalsToView(lokals);
    addLokalsToOptionList(lokals);
}

function setContent(activeVisit, pageName) {
    switch (pageName) {
        case "main":
            if (activeVisit) {
                setVerificationData();
            }
            break;
        case "verify":
            const lokalUid = window.localStorage.getItem('q-lokaluid');
            if (lokalUid !== undefined) {
                setLokalNameVerify(lokalUid);
            }
            break;
        case "profile":
            setProfileData();
            break;
        case "visits":
            getVisits(visits => {
                // Temporary storage
                const lokals = new Map();
                const wrapper = document.getElementById("visitlist");
                if (visits.length) {
                    // Clear view
                    wrapper.innerHTML = "";
                }
                // Iterate over visits
                $.each(visits.reverse(), (idx, obj) => {
                    addVisitToList(obj, lokals, wrapper);
                });
            });
            break;
        case "lokals":
            getOwnedLokals(showAllLokals);
            queryRulesStatesAvailable(json => {
                rulesAvailable = json;
                addRulesToList();
            });
            queryEncryptionTargets(addEncryptionTargetsToOptionList);
            document.getElementById("lokalcountry").addEventListener("change", listenInputSelectCountry);
            setProfileData();
    }
}

function setMsg(message, form) {
    const alertField = document.createElement("div");
    alertField.classList.add("alert", "alert-warning");
    alertField.innerText = getTranslation(message);
    document.getElementById('q-form-wrapper').insertBefore(alertField, form);
}

// QR INPUT

const qrEnabled = ['checkin', 'verify'];

const qontaktQRScanner = (function () {
    let scanner;

    function createInstance() {
        const scanElem = document.getElementById('q-qr-lens');
        QrScanner.WORKER_PATH = "/js/qr-scanner-worker.min.js";
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

let lastReadInput = null;

function listenInputGetLokal(ev) {
    const btn = document.getElementById('btn-q-checkinto');
    const elem = ev.target;
    const text = elem.value.toString();
    if (text === lastReadInput) return; // Don't re-query
    lastReadInput = text;
    if (!text.match(uidPattern)) {
        elem.classList.add('bg-warning');
        elem.classList.remove('bg-success', 'bg-danger');
        btn.setAttribute('disabled', 'true');
    } else {
        elem.classList.remove('bg-warning');
        queryLokalData(text, function (response) {
            if (response !== undefined) {
                elem.classList.remove('bg-danger');
                elem.classList.add('bg-success');
                document.getElementById('q-qr-lens-friendly').value = response.name;
                btn.removeAttribute('disabled');
                document.getElementById('lb-q-savevisit-text').innerText = formatLokalForCheckin(response);
            } else {
                elem.classList.remove('bg-success');
                elem.classList.add('bg-danger');
                document.getElementById('q-qr-lens-friendly').value = "";
                btn.setAttribute('disabled', 'true');
            }
        });
    }
}

//const uidPattern = "[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[34][0-9a-fA-F]{3}-[89ab][0-9a-fA-F]{3}-[0-9a-fA-F]{12}";
// TODO: This is for debugging only
const uidPattern = "[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}";
const longPattern = "[0-9]{1,19}";
const verifyRegex = "///" + uidPattern + "/" + uidPattern + "//" + longPattern + "///";

function setLokalNameVerify(text) {
    const friendlyLokal = document.getElementById('q-qr-lens-friendly-lokal');
    queryLokalData(text, function (response) {
        if (response !== undefined) {
            friendlyLokal.classList.remove('bg-warning');
            friendlyLokal.classList.add('bg-success');
            window.setTimeout(() => friendlyLokal.classList.remove('bg-success'), 1000);
            // Save for future reference
            window.localStorage.setItem('q-lokaluid', text);
            friendlyLokal.value = response.name;
        } else {
            window.localStorage.removeItem('q-lokaluid');
        }
    });
}

function setVerificationResult(text) {
    const friendlyResult = document.getElementById('q-qr-lens-friendly-result');
    const lokalUid = window.localStorage.getItem('q-lokaluid');
    if (lokalUid === null) {
        window.alert(getTranslation("scanLokalFirst"));
    } else {
        queryVerification(lokalUid, text, function (response) {
            if (response) {
                // Set output color
                friendlyResult.classList.remove('bg-danger');
                friendlyResult.classList.add('bg-success');
                // Timeout to allow batch processing
                window.setTimeout(() => friendlyResult.classList.remove('bg-success'), 1000);
                friendlyResult.value = getTranslation('yes');
            } else {
                // Set output color
                friendlyResult.classList.remove('bg-success');
                friendlyResult.classList.add('bg-danger');
                // Timeout to allow batch processing
                window.setTimeout(() => friendlyResult.classList.remove('bg-danger'), 1000);
                friendlyResult.value = getTranslation('no');
            }
        });
    }
}

function listenInputVerify(ev) {
    const elem = ev.target;
    const text = elem.value.toString();
    const friendly = document.getElementById('q-qr-lens-friendly-result');
    if (text === lastReadInput) return; // Don't re-query
    lastReadInput = text;
    if (!text.match(verifyRegex) && !text.match(uidPattern)) {
        // Warning in Input field for not matching regex
        elem.classList.add('bg-warning');
        // Timeout to allow batch processing
        window.setTimeout(() => friendly.classList.remove('bg-warning'), 1000);
        // Warning in Output field for bad value read
        friendly.classList.remove('bg-success', 'bg-danger');
        friendly.classList.add('bg-warning');
        friendly.value = getTranslation('no');
    } else if (text.match(verifyRegex)) {
        // Scanned a verification code
        elem.classList.remove('bg-warning');
        // Query verification state and set content
        setVerificationResult(text);
    } else {
        // Scanned a Lokal Code
        elem.classList.remove('bg-warning');
        // Query Lokal Data and set content
        setLokalNameVerify(text);
    }
}

function listenInputSelectCountry(ev) {
    const countryCode = ev.target.value;
    const stateField = document.getElementById("lokalstate");
    if (countryCode) {
        // User selected a country -> replace all children
        stateField.innerHTML = "";
        stateField.appendChild(document.createElement("option"));
        $.each(rulesAvailable[countryCode], (idx, value) => {
            const optionElement = document.createElement("option");
            optionElement.value = value;
            optionElement.innerText = getTranslation("loc-" + countryCode + "-" + value);
            stateField.appendChild(optionElement);
        });
        stateField.removeAttribute("disabled");
    } else {
        // User selected no country -> remove all children
        stateField.innerHTML = "";
        stateField.appendChild(document.createElement("option"));
        stateField.setAttribute("disabled", "disabled");
    }
}

// AUTOSTART

window.onload = function () {
    if ('serviceWorker' in navigator) navigator.serviceWorker.register('/stub-sw.js');
    redirectVerify();
    loadUserUid();  // async
    preferredLang = getLanguagePreference();
    setTranslations();
    document.documentElement.lang = preferredLang;
    // Wait asynchronously for userUid (See https://stackoverflow.com/a/56216283)
    (async () => {
        // query each 25 ms until userUid received
        while (userUid === null) await new Promise(resolve => setTimeout(resolve, 25));
        redirectProfile(getPageName());
        hasActiveVisit(function (response) {
            redirectForbidden(response, getPageName());
            setButtonStates(response, getPageName());
            setContent(response, getPageName());
        });
    })();
    initializeQR(getPageName());
}

// ROUTING
const forbiddenActive = ['checkin'];
const forbiddenInactive = ['myvisit'];

function redirectForbidden(activeVisit, pageName) {
    if (activeVisit) {
        if (forbiddenActive.includes(pageName)) document.location = 'index.html';
    } else {
        if (forbiddenInactive.includes(pageName)) document.location = 'index.html';
    }
}

const kratosLogoutEndpoint = "/.ory/kratos/public/self-service/browser/flows/logout";
const kratosSessionEndpoint = "/.ory/kratos/public/sessions/whoami";

function redirectVerify() {
    $.get(kratosSessionEndpoint).then(response => {
        if (!response.identity.verifiable_addresses[0].verified) {
            window.location.replace("/auth/verify?first=true");
        }
    });
}

const qontaktIDMEndpoint = "/api/v1/user/identity";

function redirectProfile(pageName) {
    if (pageName !== "profile") {
        $.get(qontaktIDMEndpoint + "?userUid=" + userUid).then(json => {
            if (!json) {
                window.location.replace("/profile?create");
            }
        });
    }
}

const nextAction = new Map(Object.entries({
    "btn-q-checkin": function () {
        window.location.replace("checkin");
    },
    "btn-q-closevisit": function () {
        getActiveVisit(function (visit) {
            if (visit === undefined) {
                window.alert(getTranslation("hasCheckoutError"));
            } else {
                performCheckout(visit.visitUid, function (response) {
                    window.location.reload(false);
                });
            }
        });
    },
    /*"btn-q-visitdetail": function () {
        window.location.replace("myvisit");
    },*/
    "btn-q-datadetail": function () {
        window.location.replace("profile");
    },
    "btn-q-logout": function () {
        window.location.replace(kratosLogoutEndpoint);
    },
    "btn-q-verify": function () {
        window.location.replace("verify");
    },
    "btn-q-back": function () {
        window.location.replace("/");
    },
    "btn-q-savevisit": function () {
        performCheckin(document.getElementById('q-qr-lens-alt').value, function (response) {
            // If we got here, everything went okay.
            window.location.replace("/");
        });
    },
    "btn-q-retry-offline": function () {
        window.location.replace("/");
    },
    "btn-q-save-profile": function () {
        saveProfile(document.getElementById('q-form'), function (response) {
            // If we got here, everything went okay.
            window.location.replace("/");
        });
    },
    "btn-q-listvisits": function () {
        window.location.replace("visits");
    },
    "btn-q-listlokals": function () {
        window.location.replace("lokals");
    },
    "btn-q-sendcreatelokal": function () {
        saveLokal(document.getElementById('q-form'), function (response) {
            // If we got here, everything went okay.
            $('#createLokal').modal('hide');
            document.getElementById("lb-q-password-text").innerText = getTranslation("passwordLong").replace("/pwd/", response);
            $('#showPassword').modal('show');
        })
    },
    "btn-q-close": function () {
        window.location.reload(false);
    },
    "btn-q-performexport": function () {
        performExport(document.getElementById('q-export'));
    }
}))

function showNext(item) {
    try {
        nextAction.get(item.id)();
    } catch (e) {
        console.log("Q-UI: No action defined for " + item.id)
    }

}

// COMMUNICATION

let userUid = null;

// TODO
function loadUserUid() {
    $.get("/api/v1/user/whoami", function (data, status) {
        // There is only one possible outcome, such that the simplified version should work.
        if (status !== "success") {
            window.alert(status, getTranslation('genericError'));
        } else {
            userUid = data;
            console.debug("Q-UI: UserUid: " + data);
        }
    });
}

const qontaktLokalEndpoint = "/api/v1/host/lokal";

/**
 * Get the data of a single lokal
 * @param lokalUid UID of Lokal
 * @param callback fn(lokalData | undefined)
 */
function queryLokalData(lokalUid, callback) {
    console.debug("Q-UI: Anonymously requesting Lokal " + lokalUid);
    let reqUrlParams = "?lokalUid=" + lokalUid;
    $.ajax(qontaktLokalEndpoint + reqUrlParams, {
        method: "GET",
        statusCode: {
            200: function (response) {
                console.debug("Q-UI: Request Lokals OK, found: " + String(response.length > 0));
                callback(response[0]);
            },
            401: function (response) {
                console.debug("Q-UI: Unauthorized");
                window.alert(getTranslation("unauthorizedError"));
            },
            403: function (response) {
                console.debug("Q-UI: Forbidden");
                window.alert(getTranslation("forbiddenError"));
            }
        }
    });
}

/**
 * Check whether the user has an active visit
 * @param callback fn(bool)
 */
function hasActiveVisit(callback) {
    getActiveVisit(function (visit) {
        callback(visit !== undefined);
    });
}

/**
 * Get the active visit of an user
 * @param callback fn(visitdata | undefined)
 */
function getActiveVisit(callback) {
    getVisits(function (response) {
        callback(
            $.grep(response, function (elem) {
                // An active visit is defined as a visit without checkOut
                return elem.checkOut === null;
            })[0]
        );
    });
}

/**
 * Get all visits of active user
 * @param callback fn(JSON)
 */
function getVisits(callback) {
    console.debug("Q-UI: Requesting Visits");
    let reqUrlParams = "?user_uid=" + userUid;
    $.ajax("/api/v1/user/visit" + reqUrlParams, {
        statusCode: {
            200: function (response) {
                console.debug("Q-UI: Request Visits OK, got " + response.length);
                callback(response);
            },
            400: function (response) {
                console.warn("Q-UI: Bad response: No such visit.");
                window.alert(getTranslation("genericError"));
            },
            401: function (response) {
                console.debug("Q-UI: Unauthorized");
                window.alert(getTranslation("unauthorizedError"));
            },
            403: function (response) {
                console.debug("Q-UI: Forbidden");
                window.alert(getTranslation("forbiddenError"));
            }
        }
    });
}

/**
 * Perform a checkin
 * @param lokalUid lokal to checkin at
 * @param callback fn(JSON)
 */
function performCheckin(lokalUid, callback) {
    console.debug("Q-UI: Visiting " + lokalUid);
    let reqUrlParams = "?user_uid=" + userUid + "&lokal_uid=" + lokalUid;
    $.ajax("/api/v1/user/visit" + reqUrlParams, {
        method: "POST",
        statusCode: {
            201: function (response) {
                console.debug("Q-UI: Create Visit OK");
                callback(response);
            },
            401: function (response) {
                console.debug("Q-UI: Unauthorized");
                window.alert(getTranslation("unauthorizedError"));
            },
            403: function (response) {
                console.debug("Q-UI: Forbidden");
                window.alert(getTranslation("forbiddenError"));
            },
            409: function (response) {
                console.warn("Q-UI: User has Visit");
                window.alert(getTranslation("hasVisitError"));
            }
        }
    });
}

/**
 * Perform a checkout
 * @param visitUid visit to close
 * @param callback fn(JSON)
 */
function performCheckout(visitUid, callback) {
    console.debug("Q-UI: Closing Visit" + visitUid);
    let reqUrlParams = "?user_uid=" + userUid + "&visit_uid=" + visitUid;
    $.ajax("/api/v1/user/visit" + reqUrlParams, {
        method: "PUT",
        statusCode: {
            200: function (response) {
                if (response) {
                    console.debug("Q-UI: Close Visit OK");
                    callback(response);
                } else {
                    console.warn("Q-UI: No such visit.")
                    window.alert(getTranslation("genericError"));
                }
            },
            400: function (response) {
                console.warn("Q-UI: Visit has already been closed.");
                window.alert(getTranslation("hasCheckoutError"));
                // Callback can be executed, because this can happen in multisession use.
                callback(response);
            },
            401: function (response) {
                console.debug("Q-UI: Unauthorized");
                window.alert(getTranslation("unauthorizedError"));
            },
            403: function (response) {
                console.debug("Q-UI: Forbidden");
                window.alert(getTranslation("forbiddenError"));
            }
        }
    });
}

/**
 * Query verfication string
 * @param callback fn(string)
 */
function getVerificationString(callback) {
    console.debug("Q-UI: Getting verification");
    let reqUrlParams = "?user_uid=" + userUid;
    $.ajax("/api/v1/user/verify" + reqUrlParams, {
        method: "GET",
        statusCode: {
            200: function (response) {
                console.debug("Q-UI: Get Verification OK");
                callback(response);
            },
            400: function (response) {
                console.warn("Q-UI: No visit to verify.");
                window.alert(getTranslation("hasNoVisitError"));
            },
            401: function (response) {
                console.debug("Q-UI: Unauthorized");
                window.alert(getTranslation("unauthorizedError"));
            },
            403: function (response) {
                console.debug("Q-UI: Forbidden");
                window.alert(getTranslation("forbiddenError"));
            }
        }
    });
}

/**
 * Verify a given verification string
 * @param lokalUid Uid of Lokal that wants to verify
 * @param veriString verification string
 * @param callback fn(bool)
 */
function queryVerification(lokalUid, veriString, callback) {
    console.debug("Q-UI: Verifying");
    let reqUrlParams = "?lokalUid=" + lokalUid + "&qrData=" + veriString;
    $.ajax("/api/v1/host/lokal/verify" + reqUrlParams, {
        method: "GET",
        statusCode: {
            200: function (response) {
                console.debug("Q-UI: Verification was possible");
                callback(response);
            },
            401: function (response) {
                console.debug("Q-UI: Unauthorized");
                window.alert(getTranslation("unauthorizedError"));
            }
        }
    });
}


function saveProfile(form, callback) {
    let method;
    if (hasCreate()) {
        method = "POST";
    } else {
        method = "PUT";
    }
    if (form.reportValidity()) {
        $.ajax(qontaktIDMEndpoint, {
            method: method,
            data: JSON.stringify($(form).serializeObject()),
            contentType: "application/json",
            statusCode: {
                200: function (response) {
                    console.debug("Q-UI: Save profile success");
                    callback(response);
                },
                201: function (response) {
                    console.debug("Q-UI: Create profile success");
                    callback(response);
                },
                400: function (response) {
                    window.alert(getTranslation("invalidData"));
                },
                409: function (response) {
                    window.location.replace("/profile");
                }
            }
        });
    }
}

/**
 * Query for Map of Country -> State where Qontakt is supported
 * @param callback fn(map)
 */
function queryRulesStatesAvailable(callback) {
    console.debug("Q-UI: Query known states");
    $.ajax("/api/v1/host/rules/known", {
        method: "GET",
        statusCode: {
            200: function (response) {
                console.debug("Q-UI: Loaded known states");
                callback(response);
            }
        }
    })
}

// Adapted from https://stackoverflow.com/a/24012884
function serializeForm(form) {
    return $(form).serializeArray().reduce(function(obj, item) {
        if (item.name.includes(".")) {
            const nameArr = item.name.split(".");
            if (!obj[nameArr[0]]) {
                obj[nameArr[0]] = {};
            }
            obj[nameArr[0]][nameArr[1]] = item.value;
        } else {
            obj[item.name] = item.value;
        }
        return obj;
    }, {});
}

/**
 * Save a Lokal from form
 * @param form form containing Lokal Data
 * @param callback fn(uuid)
 */
function saveLokal(form, callback) {
    if (form.reportValidity()) {
        $.ajax(qontaktLokalEndpoint, {
            method: "POST",
            data: JSON.stringify(serializeForm(form)),
            contentType: "application/json",
            statusCode: {
                201: function (response) {
                    console.debug("Q-UI: Create lokal success");
                    callback(response);
                },
                401: function (response) {
                    console.debug("Q-UI: Unauthorized");
                    window.alert(getTranslation("unauthorizedError"));
                },
                403: function (response) {
                    console.debug("Q-UI: Forbidden");
                    window.alert(getTranslation("forbiddenError"));
                },
                409: function (response) {
                    window.location.replace("/profile");
                }
            }
        });
    }
}

/**
 * Get all lokals of active user
 * @param callback fn(JSON)
 */
function getOwnedLokals(callback) {
    $.get(qontaktLokalEndpoint + "?userUid=" + userUid).then(response => callback(response));
}

function downloadMarketing(elem) {
    window.location = qontaktLokalEndpoint + "/leaflet" + "?lokalUid=" + $(elem).data("q-lokaluid");
    return false; //this is critical to stop the click event which will trigger a normal file download!
}

/**
 * Get all encryption targets
 * @param callback fn(JSON)
 */
function queryEncryptionTargets(callback) {
    $.get("/api/v1/crypto/pki/keys").then(response => callback(response));
}

function performExport(form) {
    const jsonForm = serializeForm(form);
    const resultUrl = "/api/v1/host/lokal/print?lokalUid=" + jsonForm.lokalUid
        + "&password=" + jsonForm.passwordexport
        + "&publicKeyUid=" + jsonForm.publicKeyUid;
    $.ajax(resultUrl, {
        method: "GET",
        statusCode: {
            200: function () {
                window.location = resultUrl;
            },
            401: function () {
                console.debug("Q-UI: Unauthorized");
                window.alert(getTranslation("unauthorizedError"));
            },
            403: function () {
                window.alert(getTranslation("wrongPassword"));
            }
        }
    })
}