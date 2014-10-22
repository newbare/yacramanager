module.exports = function(grunt) {

	require('load-grunt-tasks')(grunt);
	
	grunt
			.initConfig({
				jshint : {
					options : {
						devel : true,
						eqnull : false,
						jquery : true,
						newcap : false,
						strict : false,
						globalstrict : true,
						force : true,
						globals : {
							angular : false,
							_userId : false,
							_contextPath : false,
							_userCompanyId : false,
							moment : false,
							getUserInitials : false,
							jsPDF : false
						}
					},
					all : [ 'src/main/webapp/assets/js/app/**/*.js',
							'src/main/webapp/assets/js/yacra.js' ]
				},
				clean: {
				      dist: {
				        files: [{
				          dot: true,
				          src: [
				            'src/main/webapp/assets/dist/*'
				          ]
				        }]
				      },
				      server: '.tmp'
				    },
				concat : {
					options : {
						separator : ';',
					},
					dist : {
						files : {
							'src/main/webapp/assets/dist/js/yacra.js' : [
									'src/main/webapp/assets/bower_components/jquery/dist/jquery.min.js',
									'src/main/webapp/assets/bower_components/jquery-ui/jquery-ui.min.js',
									'src/main/webapp/assets/bower_components/angular/angular.js',
									'src/main/webapp/assets/bower_components/bootstrap/dist/js/bootstrap.min.js',
									'src/main/webapp/assets/js/yacra.js',
									'src/main/webapp/assets/js/app/constants.js',

							],
							'src/main/webapp/assets/dist/js/yacra-login.js' : [
									'src/main/webapp/assets/bower_components/jquery/dist/jquery.min.js',
									'src/main/webapp/assets/bower_components/jquery-ui/jquery-ui.min.js',
									'src/main/webapp/assets/bower_components/angular/angular.js',
									'src/main/webapp/assets/bower_components/bootstrap/dist/js/bootstrap.min.js',

									'src/main/webapp/assets/js/app/login.js', ],
							'src/main/webapp/assets/dist/js/yacra-app.js' : [
									'src/main/webapp/assets/bower_components/jquery/dist/jquery.min.js',
									'src/main/webapp/assets/bower_components/jquery-ui/jquery-ui.min.js',
									'src/main/webapp/assets/bower_components/angular/angular.js',
									'src/main/webapp/assets/bower_components/bootstrap/dist/js/bootstrap.min.js',
									'src/main/webapp/assets/js/app/*.js' ]
						}
					},
				},
				cssmin : {
					combine : {
						files : {
							'src/main/webapp/assets/dist/css/yacra.min.css' : [ 
					                'src/main/webapp/assets/bower_components/bootstrap/dist/css/bootstrap.min.css',
					                'src/main/webapp/assets/bower_components/fontawesome/css/font-awesome.min.css',
									'src/main/webapp/assets/css/yacra-fonts.css',
									'src/main/webapp/assets/css/yacranav.css',
									'src/main/webapp/assets/css/yacrafooter.css'
									],
							'src/main/webapp/assets/dist/css/yacra-login.min.css' : [ 
                                    'src/main/webapp/assets/bower_components/bootstrap/dist/css/bootstrap.min.css',
 					                'src/main/webapp/assets/bower_components/fontawesome/css/font-awesome.min.css',
 					                'src/main/webapp/assets/bower_components/angular-motion/dist/angular-motion.css',
 					               'src/main/webapp/assets/css/signin.css',
 					                'src/main/webapp/assets/css/yacra-fonts.css',
 									'src/main/webapp/assets/css/yacranav.css',
 									'src/main/webapp/assets/css/yacrafooter.css', 
   					                ],
							'src/main/webapp/assets/dist/css/yacra-app.min.css' : [ 
									'src/main/webapp/assets/bower_components/bootstrap/dist/css/bootstrap.min.css',
									'src/main/webapp/assets/bower_components/fontawesome/css/font-awesome.min.css',
									'src/main/webapp/assets/bower_components/bootstrap-additions/dist/bootstrap-additions.min.css',
									'src/main/webapp/assets/bower_components/angular-motion/dist/angular-motion.css',
									'src/main/webapp/assets/bower_components/angular-loading-bar/build/loading-bar.css',
									'src/main/webapp/assets/bower_components/bootstrap-daterangepicker/daterangepicker-bs3.css',
									'src/main/webapp/assets/bower_components/ng-table/ng-table.min.css',
									'src/main/webapp/assets/bower_components/angular-xeditable/dist/css/xeditable.css',
									'src/main/webapp/assets/bower_components/angular-bootstrap-colorpicker/css/colorpicker.css',
									'src/main/webapp/assets/bower_components/ngQuickDate/dist/ng-quick-date.css',
									'src/main/webapp/assets/bower_components/ngQuickDate/dist/ng-quick-date-default-theme.css',
									'src/main/webapp/assets/bower_components/fullcalendar/fullcalendar.css',
									'src/main/webapp/assets/bower_components/ng-finder/examples/css/elfinder.css',
									'src/main/webapp/assets/bower_components/chosen/chosen.min.css',
									'src/main/webapp/assets/bower_components/jquery.gritter/css/jquery.gritter.css',
									'src/main/webapp/assets/css/gritter.css',
									'src/main/webapp/assets/css/app.css',
									'src/main/webapp/assets/css/flags/flags.css',
									'src/main/webapp/assets/css/angular-criterias.css',
									'src/main/webapp/assets/css/yacra-fonts.css',
									'src/main/webapp/assets/css/yacranav.css',
									'src/main/webapp/assets/css/yacrafooter.css',
 					                ]
						}
					}
				},
				uglify : {
					options : {
						mangle : false
					},
					dist : {
						files : {
							'src/main/webapp/assets/dist/js/yacra.js' : [
									'src/main/webapp/assets/bower_components/jquery/dist/jquery.min.js',
									'src/main/webapp/assets/bower_components/jquery-ui/jquery-ui.min.js',
									'src/main/webapp/assets/bower_components/angular/angular.js',
									'src/main/webapp/assets/bower_components/bootstrap/dist/js/bootstrap.min.js',
									'src/main/webapp/assets/js/yacra.js',
									'src/main/webapp/assets/js/app/constants.js',

							],
							'src/main/webapp/assets/dist/js/yacra-login.js' : [
									'src/main/webapp/assets/bower_components/jquery/dist/jquery.min.js',
									'src/main/webapp/assets/bower_components/jquery-ui/jquery-ui.min.js',
									'src/main/webapp/assets/bower_components/angular/angular.js',
									'src/main/webapp/assets/bower_components/bootstrap/dist/js/bootstrap.min.js',

									'src/main/webapp/assets/js/app/login.js', ],
							'src/main/webapp/assets/dist/js/yacra-app.js' : [
									'src/main/webapp/assets/bower_components/jquery/dist/jquery.min.js',
									'src/main/webapp/assets/bower_components/jquery-ui/jquery-ui.min.js',
									'src/main/webapp/assets/bower_components/angular/angular.js',
									'src/main/webapp/assets/bower_components/bootstrap/dist/js/bootstrap.min.js',
									'src/main/webapp/assets/js/app/*.js' ]
						}
					}
				},
				
				 useminPrepare: {
				      html: [ 'src/main/webapp/views/index.html','src/main/webapp/views/app/index.html','src/main/webapp/views/auth/login.html' ]
				    },
				    usemin : {
					html : [ 'src/main/webapp/views/index.html','src/main/webapp/views/app/index.html','src/main/webapp/views/auth/login.html' ]
				}
				
			});



	grunt.registerTask('default', [ 'jshint','useminPrepare', 'uglify','cssmin','usemin' ]);
	grunt.registerTask('clean-dist', [ 'clean:dist' ]);
};