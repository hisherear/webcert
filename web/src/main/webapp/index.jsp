<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<!DOCTYPE html>
<html lang="sv" id="ng-app" ng-app="webcertIndex">
<head>

<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=Edge" />
<meta name="ROBOTS" content="nofollow, noindex" />
<meta name="viewport" content="width=device-width, initial-scale=1">

<title><spring:message code="application.name" /></title>

<link rel="stylesheet" href="/web/webjars/bootstrap/3.1.1/css/bootstrap.min.css" />
<link rel="stylesheet" href="/web/webjars/bootstrap/3.1.1/css/bootstrap-theme.min.css" />
<link rel="stylesheet" href="/web/webjars/common/webcert/css/inera-webcert.css">
<link rel="stylesheet" href="/web/webjars/common/css/inera-certificate.css">

<c:choose>
  <c:when test="${useMinifiedJavaScript == 'true'}">
    <script type="text/javascript" src="/web/webjars/angularjs/1.2.14/angular.min.js"></script>
    <script type="text/javascript" src="/web/webjars/angularjs/1.2.14/i18n/angular-locale_sv-se.js"></script>
    <script type="text/javascript" src="/web/webjars/angular-ui-bootstrap/0.11.2/ui-bootstrap-tpls.min.js"></script>
  </c:when>
  <c:otherwise>
    <script type="text/javascript" src="/web/webjars/angularjs/1.2.14/angular.js"></script>
    <script type="text/javascript" src="/web/webjars/angularjs/1.2.14/i18n/angular-locale_sv-se.js"></script>
    <script type="text/javascript" src="/web/webjars/angular-ui-bootstrap/0.11.2/ui-bootstrap-tpls.js"></script>
  </c:otherwise>
</c:choose>

<script type="text/javascript">
  angular.module('webcertIndex', [ 'ui.bootstrap' ])
      .controller('IndexController', ['$scope', '$sce', function ($scope, $sce) {
        $scope.showLoginDescText =  $sce.trustAsHtml('<span class="glyphicon glyphicon-chevron-down"></span> Visa mer om inloggning');
        $scope.toggleShowLoginDesc = function() {
          $scope.showLoginDesc = !$scope.showLoginDesc;
          if ($scope.showLoginDesc)
            $scope.showLoginDescText =  $sce.trustAsHtml('<span class="glyphicon glyphicon-chevron-up"></span> Visa mindre');
          else
            $scope.showLoginDescText =  $sce.trustAsHtml('<span class="glyphicon glyphicon-chevron-down"></span> Visa mer om inloggning');
        }
      }]);
</script>

</head>
<body class="start" id="indexPage" ng-controller="IndexController">
  <div class="container-fluid">
    <div class="content-container">
      <div class="row">
        <div class="col-xs-6">
          <img class="pull-right" src="/img/webcert_big.png" />
        </div>
        <div class="col-xs-6">
          <h1>Välkommen till Webcert</h1>
          <p>Webcert är en tjänst för att utfärda elektroniska läkarintyg. I Webcert kan du skriva läkarintyg och kommunicera med Försäkringskassan om läkarintyg FK 7263. För närvarande stödjer tjänsten följande intyg:</p>
          <ul>
            <li>Läkarintyg FK 7263</li>
            <li>Transportstyrelsens läkarintyg</li>
            <li>Transportstyrelsens läkarintyg, diabetes</li>
          </ul>
          <p>För att logga in i Webcert behövs SITHS-kort eller e-legitimation.</p>
          <div collapse="!showLoginDesc" class="collapse">
            <h2>Om inloggning</h2>
            <p>Om du arbetar för ett landsting eller en region ska du logga in med SITHS-kort, är du privatläkare väljer du inloggning med e-legitimation.</p>
            <p>Inloggning med SITHS-kort kräver även medarbetaruppdrag "Vård och behandling" i Hälso- och sjukvårdens adressregister, HSA-katalogen.</p>
            <p>Inloggning med e-legitimation kräver att du är läkare enligt Socialstyrelsens register över legitimerad hälso- och sjukvårdspersonal, HOSP.</p>
            <h3>Inloggning med SITHS-kort</h3>
            <p>Om du som arbetar för ett landsting eller en region inte kan logga in kan det bero på att HSA-katalogen inte är uppdaterad och att du därmed saknar medarbetaruppdraget ”Vård och behandling”.</p>
            <h3>Privatläkare med e-legitimation</h3>
            <p>Om du som privatläkare inte kan logga in kan det bero på att information om din yrkeslegitimation ännu inte har hämtas från Socialstyrelsens register över legitimerad hälso- och sjukvårdspersonal, HOSP. Information inhämtas varannan vecka.</p>
            <h3>Support och kundservice</h3>
            <p>Om du tror att något är fel kan du kontakta <a href="http://www.inera.se/KONTAKT_KUNDSERVICE/Felanmalan-och-support/">Ineras Nationella kundservice</a>. Kontrollera gärna följande innan du kontaktar supporten.</p>
            <ul>
              <li>att SITHS-kortet eller e-legitimationen sitter i kortläsaren</li>
              <li>att du angett rätt lösenord</li>
            </ul>
            <h2>Om e-legitimation</h2>
            <p>Här hittar du <a href="https://minavardkontakter.se/C125755F00329208/p/KONT-8ZSGV8?opendocument">mer information om e-legitimation</a> och om hur du gör för att <a href="http://www.e-legitimation.se">skaffa en e-legitimation</a>.</p>
            <p>För att kunna använda Webcerts funktioner behöver du någon av följande webbläsare:</p>
            <ul>
              <li>Internet Explorer 8 eller efterföljande versioner.</li>
              <li>Google Chrome 30 eller efterföljande versioner.</li>
              <li>Safari 5 eller efterföljande versioner.</li>
              <li>Firefox 24 eller efterföljande versioner.</li>
            </ul>
            <p>Du måste även ha JavaScript aktiverat i din webbläsare för att kunna använda Webcert.</p>
          </div>
          <p><a href="#" ng-click="toggleShowLoginDesc()" ng-bind-html="showLoginDescText"><span class="glyphicon glyphicon-chevron-down"></span> Visa mer om inloggning</a></p>
          <p><a href="/saml/login" class="btn btn-success" id="loginBtn">Logga in</a></p>
        </div>
      </div>
    </div>

    <div class="content-footer">
      <p>Webcert använder kakor. <a href="#" ng-click="showCookieText = !showCookieText">Läs mer om Kakor (cookies)</a></p>
      <div collapse="!showCookieText" class="bluebox">
        <h3>Om Kakor (cookies)</h3>
        <p>
          Så kallade kakor (cookies) används för att underlätta för besökaren på webbplatsen. En kaka är en textfil som
          lagras på din dator och som innehåller information. Denna webbplats använder så kallade sessionskakor.
          Sessionskakor lagras temporärt i din dators minne under tiden du är inne på en webbsida. Sessionskakor
          försvinner när du stänger din webbläsare. Ingen personlig information om dig sparas vid användning av
          sessionskakor. Om du inte accepterar användandet av kakor kan du stänga av det via din webbläsares
          säkerhetsinställningar. Du kan även ställa in webbläsaren så att du får en varning varje gång webbplatsen
          försöker sätta en kaka på din dator.</p>
        <p>
          <strong>Observera!</strong> Om du stänger av kakor i din webbläsare kan du inte logga in i Webcert.</p>
        <p>
          Allmän information om kakor (cookies) och lagen om elektronisk kommunikation finns på Post- och telestyrelsens
          webbplats.</p>
        <p>
          <a href='http://www.pts.se/sv/Bransch/Regler/Lagar/Lag-om-elektronisk-kommunikation/Cookies-kakor/'
             target='_blank'>Mer om kakor (cookies) på Post- och telestyrelsens webbplats</a>
        </p>
      </div>
    </div>
  </div>
</body>
</html>