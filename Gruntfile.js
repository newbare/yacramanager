module.exports = function(grunt) {

	grunt
			.initConfig({
				jshint : {
					all : [ 'src/main/webapp/assets/js/app/**/*.js', 'src/main/webapp/assets/js/yacra.js' ]
				},
				concat : {
					options : {
						separator : ';',
					},
					dist : {
						files : {
							'src/main/webapp/assets/dist/yacra.js' : [ 
							         'src/main/webapp/assets/bower_components/jquery/dist/jquery.min.js',
							         'src/main/webapp/assets/bower_components/jquery-ui/jquery-ui.min.js',
							         'src/main/webapp/assets/bower_components/angular/angular.js',
							         'src/main/webapp/assets/bower_components/bootstrap/dist/js/bootstrap.min.js',
							         'src/main/webapp/assets/js/yacra.js',
							         'src/main/webapp/assets/js/app/constants.js',
							         
							         ],
							'src/main/webapp/assets/dist/yacra-login.js' : 
								[ 
									'src/main/webapp/assets/bower_components/jquery/dist/jquery.min.js',
									'src/main/webapp/assets/bower_components/jquery-ui/jquery-ui.min.js',
									'src/main/webapp/assets/bower_components/angular/angular.js',
									'src/main/webapp/assets/bower_components/bootstrap/dist/js/bootstrap.min.js',
									
									'src/main/webapp/assets/js/app/login.js',
								  ],
							'src/main/webapp/assets/dist/yacra-app.js' : [
									'src/main/webapp/assets/bower_components/jquery/dist/jquery.min.js',
									'src/main/webapp/assets/bower_components/jquery-ui/jquery-ui.min.js',
									'src/main/webapp/assets/bower_components/angular/angular.js',
									'src/main/webapp/assets/bower_components/bootstrap/dist/js/bootstrap.min.js',
									'src/main/webapp/assets/js/app/*.js' ]
						}
					},
				},
				uglify : {
					options :{
						mangle : false
					},
					dist : {
						files : {
							'src/main/webapp/assets/dist/yacra.js' : [ 
							         'src/main/webapp/assets/bower_components/jquery/dist/jquery.min.js',
							         'src/main/webapp/assets/bower_components/jquery-ui/jquery-ui.min.js',
							         'src/main/webapp/assets/bower_components/angular/angular.js',
							         'src/main/webapp/assets/bower_components/bootstrap/dist/js/bootstrap.min.js',
							         'src/main/webapp/assets/js/yacra.js',
							         'src/main/webapp/assets/js/app/constants.js',
							         
							         ],
							'src/main/webapp/assets/dist/yacra-login.js' : 
								[ 
									'src/main/webapp/assets/bower_components/jquery/dist/jquery.min.js',
									'src/main/webapp/assets/bower_components/jquery-ui/jquery-ui.min.js',
									'src/main/webapp/assets/bower_components/angular/angular.js',
									'src/main/webapp/assets/bower_components/bootstrap/dist/js/bootstrap.min.js',
									
									'src/main/webapp/assets/js/app/login.js',
								  ],
							'src/main/webapp/assets/dist/yacra-app.js' : [
									'src/main/webapp/assets/bower_components/jquery/dist/jquery.min.js',
									'src/main/webapp/assets/bower_components/jquery-ui/jquery-ui.min.js',
									'src/main/webapp/assets/bower_components/angular/angular.js',
									'src/main/webapp/assets/bower_components/bootstrap/dist/js/bootstrap.min.js',
									'src/main/webapp/assets/js/app/*.js' ]
						}
					}
				}
			});

	grunt.loadNpmTasks('grunt-contrib-concat');
	grunt.loadNpmTasks('grunt-contrib-uglify');
	grunt.loadNpmTasks('grunt-contrib-jshint');

	grunt.registerTask('default', [ 'jshint','uglify' ]);
};