// TRANSLATIONS

const translations = new Map(Object.entries({
    "de": {
        "recoverAccount": "Passwort vergessen",
        "recover": "Passwort zurücksetzen",
        "register": "Registrieren",
        "identifier": "E-Mail-Adresse",
        "password": "Passwort",
        "login": "Anmelden",
        "email": "E-Mail-Adresse",
        "traits.email": "E-Mail-Adresse",
        "verify": "Verifikation anfordern",
        "requestNewVerification": "Verifikationslink erneut versenden",
        "requestNewRecovery": "Wiederherstellung erneut anfordern",
        "home": "Zur Anwendung",
        "verifySuccess": "Die Verifikation war erfolgreich.",
        "kratos-1060001": "Die Wiederherstellung des Accounts war erfolgreich. Bitte ändern Sie noch Ihr Passwort.",
        "kratos-1060002": "Eine E-Mail mit einem Link zur Wiederherstellung des Accounts wurde an die angegebene E-Mail-Adresse versandt.",
        "kratos-1070002": "Eine E-Mail mit einem Link zur Verifikation wurde an die angegebene E-Mail-Adresse versandt.",
        //"kratos-4000002": "Fehlendes Feld.",
        "kratos-4000003": "Die Mindestlänge für die Validierung wurde nicht eingehalten.",
        "kratos-4000004": "Die Formatvorgaben für die Validierung wurden nicht eingehalten.",
        "kratos-4000005": "Das gewählte Passwort verletzt die Passwortvorgaben oder wurde gehackt.",
        "kratos-4000006": "Die Anmeldedaten sind ungültig. Überprüfen Sie Ihre E-Mail-Adresse und Ihr Passwort.",
        "kratos-4000007": "Es existiert bereits ein Account mit diesen Benutzerdaten.",
        "kratos-4010001": "Der Anmeldevorgang ist abgelaufen. Bitte versuchen Sie es erneut.",
        "kratos-4040001": "Der Registrierungsvorgang ist abgelaufen. Bitte versuchen Sie es erneut.",
        "kratos-4050001": "Der Einstellungsvorgang ist abgelaufen. Bitte versuchen Sie es erneut.",
        "kratos-4060002": "Während der Wiederherstellung ist ein Fehler aufgetreten. Bitte versuchen Sie es erneut.",
        "kratos-4060003": "Das Token zur Wiederherstellung wurde nicht übergeben. Bitte starten Sie den Wiederherstellungsvorgang erneut.",
        "kratos-4060004": "Das Token zur Wiederherstellung ist ungültig oder wurde bereits verwendet. Bitte starten Sie den Wiederherstellungsvorgang erneut.",
        "kratos-4060005": "Der Wiederherstellungsvorgang ist abgelaufen. Bitte versuchen Sie es erneut.",
        "kratos-4070001": "Das Token zur Verifikation ist ungültig oder wurde bereits verwendet. Bitte starten Sie den Verifikationsvorgang erneut.",
        "kratos-4070002": "Die Verifikation wurde bereits erfolgreich durchgeführt und kann nicht wiederholt werden.",
        "kratos-4070003": "Während der Bestätigung ist ein Fehler aufgetreten. Bitte versuchen Sie es erneut.",
        "kratos-4070005": "Der Bestätigungsvorgang ist abgelaufen. Bitte versuchen Sie es erneut.",
        "kratos-cust-00001": "Bitte verifizieren Sie Ihre E-Mail-Addresse.",
    }
}));

const defaultLang = "de";

let preferredLang;

const translatableFields = new Map(Object.entries({
    "btn-q-recover": "recoverAccount",
    "btn-q-register": "register",
    "btn-q-login": "login",
    "btn-q-new-verification": "requestNewVerification",
    "lb-q-verify-waiting": "kratos-1070002",
    "btn-q-new-recovery": "requestNewRecovery",
    "lb-q-recover-waiting": "kratos-1060002",
    "btn-q-home": "home",
    "lb-q-verify-success": "verifySuccess",
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

function getFlowId() {
    return new URLSearchParams(location.search).get("flow")
}

function hasFlowId() {
    return getFlowId() !== null;
}

const pageNameToFlow = new Map(Object.entries({
    "login": "login",
    "register": "registration",
    "verify": "verification",
    "recover": "recovery",
}));


// OUTPUTS

const kratosApiUrl = "/.ory/kratos/public/self-service/";
const kratosApiMode = "/browser";

function setMsg(messages, form) {
    messages.forEach(msg => {
        const alertField = document.createElement("div");
        alertField.classList.add("alert", "alert-danger");
        alertField.innerText = getTranslation("kratos-" + msg.id);
        document.getElementById('q-form-wrapper').insertBefore(alertField, form);
    });
}

function setFlowDetails(flowId, pageName) {
    $.ajax(kratosApiUrl + pageNameToFlow.get(pageName) + "/flows?id=" + flowId, {
        type: "GET",
        statusCode: {
            200: function (response) {
                console.log(response);
                if (response.state === "sent_email") {
                    window.location.replace("/auth/wait-" + pageName);
                } else if (response.state === "passed_challenge") {
                    window.location.replace("/auth/" + pageName + "-success");
                }
                let formConfig;
                if (response.methods.link) {
                    formConfig = response.methods.link.config;
                } else if (response.methods.password) {
                    formConfig = response.methods.password.config;
                }
                const form = document.getElementById('q-kratos');
                form.setAttribute('method', formConfig.method);
                form.setAttribute('action', formConfig.action);
                form.childNodes.forEach(child => child.remove());
                if (pageName === "register") {
                    formConfig.fields.reverse();
                }
                formConfig.fields.forEach(field => {
                    // <div>
                    const formElementWrapper = document.createElement("div");
                    formElementWrapper.classList.add("form-group");
                    // <label>
                    const formFieldLabel = document.createElement("label");
                    formFieldLabel.setAttribute("for", field.name);
                    formFieldLabel.innerText = getTranslation(field.name);
                    formElementWrapper.appendChild(formFieldLabel);
                    // </label>
                    // <input>
                    const formFieldInput = document.createElement("input");
                    formFieldInput.classList.add("form-control");
                    formFieldInput.setAttribute("name", field.name);
                    formFieldInput.setAttribute("placeholder", getTranslation(field.name));
                    switch (field.name) {
                        case "identifier":
                        case "email":
                        case "traits.email":
                            formFieldInput.setAttribute("autocomplete", "username email");
                            formFieldInput.setAttribute("id", "username");
                            if (pageName === "verify") {
                                console.log("Q-UI: Getting verification email address");
                                $.get("/.ory/kratos/public/sessions/whoami")
                                    .then(json => {
                                            console.log(json);
                                            formFieldInput.setAttribute("value", json.identity.traits.email);
                                            formFieldInput.setAttribute("readonly", "true");
                                        });
                            }
                            break;
                        case "password":
                            formFieldInput.minLength = 6;
                            switch (pageName) {
                                case "login":
                                    formFieldInput.setAttribute("autocomplete", "current-password");
                                    formFieldInput.setAttribute("id", "current-password");
                                    break;
                                case "register":
                                    formFieldInput.setAttribute("autocomplete", "new-password");
                                    formFieldInput.setAttribute("id", "new-password");
                                    break;
                            }
                            break;
                        default:
                            break;
                    }
                    formFieldInput.setAttribute("type", field.type);
                    if (field.required) {
                        formFieldInput.setAttribute("required", field.required);
                    }
                    if (field.value) {
                        formFieldInput.setAttribute("value", field.value);
                    }
                    formElementWrapper.appendChild(formFieldInput);
                    // </input>
                    // <feedback>
                    if (field.messages) {
                        field.messages.forEach(msg => {
                            formFieldInput.classList.add("is-invalid");
                            const formFieldFeedback = document.createElement("div");
                            formFieldFeedback.classList.add("invalid-feedback");
                            formFieldFeedback.innerText = getTranslation("kratos-" + msg.id);
                            formElementWrapper.appendChild(formFieldFeedback);
                        });
                    }
                    // </feedback>
                    switch (field.type) {
                        case "hidden":
                            form.appendChild(formFieldInput);
                            break;
                        default:
                            form.appendChild(formElementWrapper);
                    }
                    // </div>
                });
                const submitButton = document.createElement("button");
                submitButton.type = "submit";
                submitButton.classList.add("btn", "btn-primary", "btn-lg", "btn-block");
                submitButton.innerText = getTranslation(pageName);
                form.appendChild(submitButton);
                if (response.messages !== null) {
                    setMsg(response.messages, form);
                } else {
                    if (pageName === "verify") {
                        setMsg([{id: "cust-00001"}], form);
                    }
                }
                if (formConfig.messages !== undefined) {
                    setMsg(formConfig.messages, form);
                }
            },
            410: function () {
                // Flow has expired
                createFlow(pageName);
            }
        }
    })
}

function createFlow(pageName) {
    window.location.replace(kratosApiUrl + pageNameToFlow.get(pageName) + kratosApiMode);
}

function setContent(pageName) {
    switch (pageName) {
        case "wait-recover":
        case "wait-verify":
        case "verify-success":
            break;
        case "verify":
            redirectVerifySuccess();
        default:
            if (!hasFlowId()) {
                createFlow(pageName);
            }
            setFlowDetails(getFlowId(), pageName);
    }
}

// AUTOSTART

window.onload = function () {
    if ('serviceWorker' in navigator) navigator.serviceWorker.register('/stub-sw.js');
    preferredLang = getLanguagePreference();
    setTranslations();
    setContent(getPageName());
}

// ROUTING

const nextAction = new Map(Object.entries({
    "btn-q-register": function () {
        window.location.replace("register");
    },
    "btn-q-login": function () {
        window.location.replace("login");
    },
    "btn-q-recover": function () {
        window.location.replace("recover");
    },
    "btn-q-new-recovery": function () {
        window.location.replace("recover");
    },
    "btn-q-new-verification": function () {
        window.location.replace("verify");
    },
    "btn-q-home": function () {
        window.location.replace("/");
    },
}))

function showNext(item) {
    try {
        nextAction.get(item.id)();
    } catch (e) {
        console.log("Q-UI: No action defined for " + item.id)
    }

}

const kratosSessionEndpoint = "/.ory/kratos/public/sessions/whoami";

function redirectVerifySuccess() {
    $.get(kratosSessionEndpoint).then(response => {
        if (response.identity.verifiable_addresses[0].verified) {
            window.location.replace("/auth/verify-success");
        }
    });
}