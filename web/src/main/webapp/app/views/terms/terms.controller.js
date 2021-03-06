angular.module('webcert').controller('webcert.TermsCtrl', ['$log', '$rootScope', '$scope', '$window', '$modal',
        '$sanitize', '$state', '$location',
        'common.AvtalProxy', 'common.UserModel',
        function($log, $rootScope, $scope, $window, $modal, $sanitize, $state, $location, AvtalProxy, UserModel) {
            'use strict';
            $scope.terms = {doneLoading:false, avtal:false};

            UserModel.termsAccepted = false;
            UserModel.transitioning = false;

            // load the avtal
            AvtalProxy.getLatestAvtal(function(avtalModel) {
                $scope.terms.avtal = avtalModel;
                $scope.terms.doneLoading = true;
            }, function() {

                $scope.terms.doneLoading = true;
            });

            function endsWith(str, suffix) {
                return str.indexOf(suffix, str.length - suffix.length) !== -1;
            }

            $scope.modalOptions = {
                terms : $scope.terms,
                modalBodyTemplateUrl : '/app/views/terms/terms.body.html',
                titleId: 'avtal.title.text',
                extraDlgClass: undefined,
                width: '600px',
                height: '90%',
                maxWidth: '600px',
                maxHeight: undefined,
                minWidth: undefined,
                minHeight: undefined,
                contentHeight: '100%',
                contentOverflowY: undefined,
                contentMinHeight: undefined,
                bodyOverflowY: 'scroll',
                buttons: [
                    {
                        name: 'approve',
                        clickFn: function() {
                            AvtalProxy.approve(
                                function() {
                                    UserModel.termsAccepted = true;
                                    $scope.modalOptions.modalInstance.dismiss('cancel');
                                    $state.transitionTo('webcert.create-index');
                                },
                                function() {
                                    UserModel.termsAccepted = false;
                                    $window.location = '/web/error';
                                }
                            );
                        },
                        text: 'avtal.approve.label',
                        id: 'acceptTermsBtn'
                    },
                    {
                        name: 'print',
                        clickFn: function() {
                            var head = '<!DOCTYPE html><html>' +
                                '<head>' +
                                '<link rel="stylesheet" href="/web/webjars/common/webcert/css/print.css" media="print">' +
                                '<title>Webcert - Användarvillkor</title>' +
                                '</head>';

                            var body = '<body onload="window.print()">' +
                                '<img class="pull-left" style="padding-bottom: 20px" src="/img/webcert_grey_small.png" />' +
                                '<p style="clear:left;padding-bottom:50px;color:#535353">' +
                                '<span style="padding-left:20px;padding-right:30px">Version : ' +
                                $scope.modalOptions.terms.avtal.avtalVersion + '</span>' +
                                '<span>Datum : ' + $scope.modalOptions.terms.avtal.versionDatum + '</span></p>' +
                                '<h1 style="color: black;font-size: 2em">Användarvillkor för Webcert</h1>' +
                                '<p style="clear:left;padding-bottom: 10px">' + $scope.modalOptions.terms.avtal.avtalText + '</p>' +
                                '<p style="clear:left;color:#535353;padding-top:50px">' + $location.absUrl() + '</p>' +
                                '</body>';

                            var footer = '</html>';

                            var template = head + body + footer;

                            if (navigator.userAgent.toLowerCase().indexOf('chrome') > -1) {
                                var popupWin = window.open('', '_blank',
                                    'width=400,scrollbars=no,menubar=no,toolbar=no,location=no,status=no,titlebar=no');
                                popupWin.window.focus();
                                popupWin.document.write(template);
                                setTimeout(function() {
                                    popupWin.close();
                                }, 100);
                                popupWin.onbeforeunload = function(event) {
                                    popupWin.close();
                                };
                                popupWin.onabort = function(event) {
                                    popupWin.document.close();
                                    popupWin.close();
                                };
                            } else {
                                var popupWin = window.open('', '_blank',
                                    'width=800,scrollbars=yes,menubar=no,toolbar=no,location=no,status=no,titlebar=no');
                                popupWin.document.open();
                                popupWin.document.write(template);
                            }
                            popupWin.document.close();

                            return true;
                        },
                        text: 'avtal.print.label',
                        id: 'printTermsBtn'
                    },
                    {
                        name: 'logout',
                        clickFn: function() {
                            if (endsWith(UserModel.user.authenticationScheme, ':fake')) {
                                $window.location = '/logout';
                            } else {
                                iid_Invoke('Logout');
                                $window.location = '/saml/logout/';
                            }
                        },
                        text: 'avtal.logout.label',
                        id: 'logoutTermsBtn'
                    }

                ],

                showClose: false

            };

        }]
);