angular.module('webcert').factory('webcert.IntygProxy',
    [ '$http', '$stateParams', '$log', 'common.statService',
        function($http, $stateParams, $log, statService) {
            'use strict';

             /*
             * Load certificate list of all certificates for a person
             */
            function _getIntygForPatient(personId, onSuccess, onError) {
                $log.debug('getIntygForPatient type:' + personId);
                var restPath = '/api/intyg/person/' + personId;
                $http.get(restPath).success(function(data, statusCode, headers) {
                    $log.debug('got data:' + data);
                    if (typeof headers('offline_mode') !== 'undefined' && headers('offline_mode') === 'true') {
                        onError(statusCode, 'info.certload.offline');
                    }
                    onSuccess(data);
                }).error(function(data, status) {
                    $log.error('error ' + status);
                    // Let calling code handle the error of no data response
                    onError(status);
                });
            }

             // Return public API for the service
            return {
                getIntygForPatient: _getIntygForPatient
            };
        }]);
