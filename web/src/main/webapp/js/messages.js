/* jshint maxlen: false, unused: false */
var wcMessages = {
    'sv': {
        'webcert.header': 'Webcert',
        'webcert.description': 'Välkommen till Webcert.',
        'dashboard.title': 'Mina andra enheter',
        'dashboard.unanswered.title': 'Frågor och svar',
        'dashboard.unsigned.title': 'Ej signerade utkast',
        'dashboard.about.title': 'Om Webcert',

        //labels
        'label.default-cert-type': 'Välj typ av intyg',

        'label.unsignedcerts': 'Ej signerade intyg',
        'label.unansweredcerts': 'Intyg med ej hanterad fråga',
        'label.readytosigncerts': 'Intyg färdiga att signera (massignering)',
        'label.showallcerts': 'Visa alla intyg',
        'label.showfewercerts': 'Visa färre intyg',
        'label.patient': 'Patient:',
        'label.signselectedcerts': 'Signera valda intyg',

        'label.confirmaddress': 'Återanvänd uppgifter',
        'label.confirmsign': 'Signera intyget',
        'label.copycert': 'Kopiera intyg',

        'print.label.signed': 'Observera! Detta är en webbutskrift av intyget och är därför inte giltigt som intyg.',
        'print.label.draft': 'Observera! Intyget är ett utkast och är därför inte giltigt.',
        'print.label.revoked': 'Observera! Intyget är makulerat och därför inte giltigt.',

        'label.makulera': 'Makulera intyg',
        'label.makulera.body' : 'Välj hur du vill makulera intyget.',
        'label.makulera.confirmation': 'Kvittens - Återtaget intyg',

        'label.qaonlywarning' : 'Du har valt att lämna frågor och svar',
        'label.qaonlywarning.body' : '<p>Observera att intyg ska utfärdas via journalsystemet och inte via Webcert.</p><p>Information i Webcert som inte är frågor och svar kan inte visas i journalsystemet.</p>',

        //certificate types
        'certificatetypes.fk7263.typename': 'Läkarintyg FK 7263',
        'certificatetypes.rli.typename': 'Intyg vid avbeställd resa',
        'certificatetypes.ts-bas.typename': 'Transportstyrelsens läkarintyg',
        'certificatetypes.ts-diabetes.typename': 'Transportstyrelsens läkarintyg, diabetes',

        //certificate help texts
        'certificatetypes.ts-bas.helptext': '<p>Transportstyrelsen läkarintyg ska användas vid förlängd giltighet av högre behörighet från 45 år, ansökan om körkortstillstånd för grupp II och III och vid ansökan om taxiförarlegitimation. Transportstyrelsen läkarintyg kan även användas när Transportstyrelsen i annat fall begärt ett allmänt läkarintyg avseende lämplighet att inneha körkort.</p>Specialistintyg finns bl.a. för alkohol, läkemedel, synfunktion, Alkolås m.m. Se <a href="http://www.transportstyrelsen.se" target="_blank">www.transportstyrelsen.se</a> därefter \"Väg\" och \"Trafikmedicin\".',
        'certificatetypes.ts-diabetes.helptext': '<p>Läkarintyg, diabetes ska användas vid diabetessjukdom. Föreskrivna krav på läkarens specialistkompetens vid diabetessjukdom framgår av 17 kap. i Transportstyrelsens föreskrifter (TSFS 2010:125) och allmänna råd om medicinska krav för innehav av körkort m.m.</p>Information om Transportstyrelsens föreskrifter finns under länken \"Väg\" och sedan länken \"Trafikmedicin\" på <a href="http://www.transportstyrelsen.se" target="_blank">www.transportstyrelsen.se</a>.',
        'certificatetypes.fk7263.helptext': 'Läkarintyget används av Försäkringskassan för att bedöma om patienten har rätt till sjukpenning. Av intyget ska det framgå hur sjukdomen påverkar patientens arbetsförmåga och hur länge patienten behöver vara sjukskriven. Läkarintyg FK 7263 ska skrivas om patientens sjukskrivningsperiod beräknas överstiga 14 dagar.',
        'certificatetypes.rli.helptext': 'Här ska det ligga en text om intyg vid avbeställd resa',

        //about texts
        'about.cookies': '<h3>Om kakor (cookies)</h3><p>Så kallade kakor (cookies) används för att underlätta för besökaren på webbplatsen. En kaka är en textfil som lagras på din dator och som innehåller information. Denna webbplats använder så kallade sessionskakor. Sessionskakor lagras temporärt i din dators minne under tiden du är inne på en webbsida. Sessionskakor försvinner när du stänger din webbläsare. Ingen personlig information om dig sparas vid användning av sessionskakor.</p><p>Om du inte accepterar användandet av kakor kan du stänga av det via din webbläsares säkerhetsinställningar. Du kan även ställa in webbläsaren så att du får en varning varje gång webbplatsen försöker sätta en kaka på din dator.</p><p><strong>Observera!</strong> Om du stänger av kakor i din webbläsare kan du inte logga in i Webcert.</p><p>Allmän information om kakor (cookies) och lagen om elektronisk kommunikation finns på Post- och telestyrelsens webbplats.</p><p><a href="http://www.pts.se/sv/Bransch/Regler/Lagar/Lag-om-elektronisk-kommunikation/Cookies-kakor/" target="_blank">Mer om kakor (cookies) på Post- och telestyrelsens webbplats</a></p>',

        //info messages
        'info.nounsignedcertsfound': '<strong>Inga ej signerade intyg hittades.</strong>',
        'info.nounansweredcertsfound': '<strong>Inga intyg med ohanterade frågor hittades.</strong>',
        'info.noreadytosigncertsfound': '<strong>Inga klarmarkerade intyg hittades.</strong>',
        'info.loadingdata': '<strong>Uppdaterar lista...</strong>',
        'info.nounanswered.qa.for.unit': '<strong>Inga ej hanterade frågor och svar för enheten.</strong>',
        'info.nocertsfound': '<strong>Inga intyg hittades.</strong>',
        'info.query.noresults': '<strong>Sökningen gav inga resultat.</strong>',
        'info.query.error': '<strong>Sökningen kunde inte utföras.</strong>',
        'info.certload.error': '<strong>Kunde inte hämta intyg.</strong>',
        'info.running.query': '<strong>Söker...</strong>',

        //error messages
        'error.unsignedcerts.couldnotbeloaded': '<strong>Kunde inte hämta ej signerade intyg.</strong>',
        'error.unansweredcerts.couldnotbeloaded': '<strong>Kunde inte hämta listan med ej hanterade frågor och svar.</strong>',
        'error.readytosigncerts.couldnotbeloaded': '<strong>Kunde inte hämta intyg klara för signering.</strong>',
        'error.failedtocreateintyg': 'Kunde inte skapa intyget. Försök igen senare.',
        'error.failedtocancelintyg': 'Kunde inte makulera intyget. Försök igen senare.',
        'error.failedtocopyintyg': 'Kunde inte kopiera intyget. Försök igen senare.',
        'error.failedtosendintyg': 'Kunde inte skicka intyget. Försök igen senare.'
    },
    'en': {
        'webcert.header': 'Webcert Application (en)'
    }
};