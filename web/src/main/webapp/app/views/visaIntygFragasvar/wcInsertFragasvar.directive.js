angular.module('webcert').directive('wcInsertQa',
    function($compile, $log) {
        'use strict';

        return {
            restrict: 'A',
            replace: true,
            scope: {
                certificateType: '@'
            },
            link: function(scope, element) {
                if(scope.certificateType === 'fk7263') {
                    $.get('/web/webjars/' + scope.certificateType +
                    '/webcert/app/views/intyg/fragasvar/fragasvar.html').then(function(file) {
                        element.html(file);
                        element.replaceWith($compile(element.html())(scope));
                    }).fail(function(error) {
                        $log.debug(error);
                    });
                }
            }
        };
    });
