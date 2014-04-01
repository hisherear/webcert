define([ 'text!directives/wcAbout.html' ], function(template) {
	'use strict';

	return [ '$rootScope', '$location', function($rootScope, $location) {
		return {
			restrict : 'A',
			transclude : true,
			replace : true,
			scope : {
				menuDefsAbout : '@'
			},
			template : template,
			controller : function($scope, $element, $attrs) {
				// Expose "now" as a model property for the template to render as todays date
				$scope.today = new Date();
				$scope.menuItems = [ {
					link : '/web/dashboard#/support/about',
					label : 'Support / kontaktinformation'
				}, {
					link : '/web/dashboard#/certificates/about',
					label : 'Intyg som stöds i Webcert'
				}, {
					link : '/web/dashboard#/faq/about',
					label : 'Vanliga frågor'
				}, {
					link : '/web/dashboard#/cookies/about',
					label : 'Om kakor (cookies)'
				} ];

				function getSubMenuName(path) {
					path = path.substring(0, path.lastIndexOf('/'));
					return path.substring(path.lastIndexOf('/') + 1);
				}

				var currentSubMenuName = getSubMenuName($location.path()) || 'index';
				$scope.currentSubMenuLabel = '';

				// set header label based on menu items label
				angular.forEach($scope.menuItems, function(menu) {
					var page = getSubMenuName(menu.link);
					if (page === currentSubMenuName) {
						$scope.currentSubMenuLabel = menu.label;
					}
				});

				$scope.isActive = function(page) {
					page = getSubMenuName(page);
					return page === currentSubMenuName;
				};
			}
		};
	} ];
});