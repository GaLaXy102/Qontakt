const translations = new Map(Object.entries({
    "de": {
        "checkIn": "Einchecken",
        "checkOut": "Auschecken",
        "myVisit": "Mein Besuch",
        "myData": "Meine Daten",
        "logout": "Abmelden",
        "verify": "Verifizieren"
    }
}));

const defaultLang = "de";

const translatableFields = new Map(Object.entries({
    "btn-q-checkin": "checkIn",
    "btn-q-checkout": "checkOut",
    "btn-q-visitdetail": "myVisit",
    "btn-q-datadetail": "myData",
    "btn-q-logout": "logout",
    "btn-q-verify": "verify"
}))

function setTranslations(lang) {
    if (!translations.has(lang)) lang = defaultLang;
    translatableFields.forEach((trl, identifier) => {
        const elem = document.getElementById(identifier);
        let text = translations.get(lang)[trl];
        if (elem != null) {
            if (text == null) {
                console.log("Q-UI: Missing translation for " + trl + " in " + lang);
                text = translations.get(defaultLang)[trl];
            }
            elem.innerText = text;
        }
    })
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

function hasActiveVisit() {
    // TODO implement me
    return false;
}

function setButtonStates(hasVisit) {
    const checkinButton = document.getElementById('btn-q-checkin');
    const checkoutButton = document.getElementById('btn-q-checkout');
    const myVisitButton = document.getElementById('btn-q-visitdetail');
    if (!hasVisit) {
        checkoutButton.remove();
        myVisitButton.setAttribute("disabled", "true");
    } else {
        checkinButton.remove();
    }
}

window.onload = function () {
    setButtonStates(hasActiveVisit());
    setTranslations(getLanguagePreference());
}

const nextAction = new Map(Object.entries({
    "btn-q-checkin": function () { window.location.href = "checkin" },
    //"btn-q-checkout": function () { console.log("Implement me!") }, // TODO
    "btn-q-visitdetail": function () { window.location.href = "myvisit" },
    "btn-q-datadetail": function () { window.location.href = "mydata" },
    //"btn-q-logout": function () { console.log("Implement me!") }, // TODO
    "btn-q-verify": function () { window.location.href = "verify" },
}))

function showNext(item) {
    try {
        nextAction.get(item.id)();
    } catch (e) {
        console.log("Q-UI: No action defined for " + item.id)
    }

}