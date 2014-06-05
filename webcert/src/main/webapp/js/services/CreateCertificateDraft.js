define([
    'angular',
    'webjars/common/webcert/js/services/statService'
], function(angular, statService) {
    'use strict';

    var moduleName = 'wc.CreateCertificateDraft';

    angular.module(moduleName, [ statService ]).
        factory(moduleName, [ '$http', '$log', statService,
            function($http, $log, statService) {
                return {

                    reset: function() {
                        this.personnummer = null;
                        this.intygType = 'default';
                        this.firstname = null;
                        this.lastname = null;
                        this.address = null;
                        this.vardEnhetHsaId = null;
                        this.vardEnhetNamn = null;
                        this.vardGivareHsaId = null;
                        this.vardGivareHsaNamn = null;
                    },

                    getNameAndAddress: function(personnummer, onSuccess) {
                        $log.debug('CreateCertificateDraft getNameAndAddress');

                        this.personnummer = personnummer;
                        if (this.personnummer === '19121212-1212' || this.personnummer === '20121212-1212') {
                            this.firstname = 'Test';
                            this.lastname = 'Testsson';
                            this.address = 'Storgatan 23';
                        } else {
                            this.firstname = null;
                            this.lastname = null;
                            this.address = null;
                        }
                        onSuccess();
                    },

                    createDraft: function(onSuccess, onError) {
                        $log.debug('_createDraft');

                        var payload = {};
                        payload.patientPersonnummer = this.personnummer;
                        payload.patientFornamn = this.firstname;
                        payload.patientEfternamn = this.lastname;
                        payload.intygType = this.intygType;
                        payload.postadress = this.address;
                        payload.vardEnhetHsaId = this.vardEnhetHsaId;
                        payload.vardEnhetNamn = this.vardEnhetNamn;
                        payload.vardGivareHsaId = this.vardGivareHsaId;
                        payload.vardGivareNamn = this.vardGivareNamn;

                        var restPath = '/api/intyg/create';
                        $http.post(restPath, payload).success(function(data) {
                            $log.debug('got callback data: ' + data);
                            onSuccess(data);
                            statService.refreshStat();

                        }).error(function(data, status) {
                            $log.error('error ' + status);
                            onError(data);
                        });
                    },

                    copyIntygToDraft: function(cert, onSuccess, onError) {
                        $log.debug('_copyIntygToDraft ' + cert.intygType + ', ' + cert.intygId);

                        var payload = {};
                        payload.sourceIntygId = cert.intygId;
                        payload.patientPersonnummer = this.personnummer;
                        payload.patientFornamn = this.firstname;
                        payload.patientEfternamn = this.lastname;
                        payload.intygType = this.intygType;
                        payload.postadress = this.address;
                        payload.vardEnhetHsaId = this.vardEnhetHsaId;
                        payload.vardEnhetNamn = this.vardEnhetNamn;
                        payload.vardGivareHsaId = this.vardGivareHsaId;
                        payload.vardGivareNamn = this.vardGivareNamn;

                        var restPath = '/api/intyg/copy';
                        $http.post(restPath, payload).success(function(data) {
                            $log.debug('got callback data: ' + data);
                            onSuccess(data);
                            statService.refreshStat();

                        }).error(function(data, status) {
                            $log.error('error ' + status);
                            onError(data);
                        });
                    }
                };
            }
        ]);

    return moduleName;
});