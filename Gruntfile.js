// Generated on 2015-03-13 using generator-jhipster 2.6.0
'use strict';
var fs = require('fs');

var parseString = require('xml2js').parseString;
// Returns the second occurence of the version number
var parseVersionFromPomXml = function() {
    var version;
    var pomXml = fs.readFileSync('pom.xml', "utf8");
    parseString(pomXml, function (err, result){
        version = result.project.version[0];
    });
    return version;
};

// usemin custom step
var useminAutoprefixer = {
    name: 'autoprefixer',
    createConfig: require('grunt-usemin/lib/config/cssmin').createConfig // Reuse cssmins createConfig
};

module.exports = function(grunt) {

	require('load-grunt-tasks')(grunt);
	require('time-grunt')(grunt);
	grunt
			.initConfig({
				yeoman: {
		            // configurable paths
		            app: require('./bower.json').appPath || 'app',
		            dist: 'src/main/webapp/dist'
		        },
		        watch: {
		            bower: {
		                files: ['bower.json'],
		                tasks: ['wiredep']
		            },
		            ngconstant: {
		                files: ['Gruntfile.js', 'pom.xml'],
		                tasks: ['ngconstant:dev']
		            },
		            styles: {
		                files: ['src/main/webapp/assets/styles/**/*.css']
		            }
		        },
		        autoprefixer: {
		        // not used since Uglify task does autoprefixer,
		        //    options: ['last 1 version'],
		        //    dist: {
		        //        files: [{
		        //            expand: true,
		        //            cwd: '.tmp/styles/',
		        //            src: '**/*.css',
		        //            dest: '.tmp/styles/'
		        //        }]
		        //    }
		        },
		        wiredep: {
		            app: {
		                src: ['src/main/webapp/views/index.html','src/main/webapp/views/app/index.html','src/main/webapp/views/auth/login.html','src/main/webapp/views/auth/register.html','src/main/webapp/views/auth/forgot-password.html'],
		                exclude: [
		                    /angular-i18n/,  // localizations are loaded dynamically
		                    /swagger-ui/
		                ]
		            }
		        },
		        browserSync: {
		            dev: {
		                bsFiles: {
		                    src : [
		                        'src/main/webapp/**/*.html',
		                        'src/main/webapp/**/*.json',
		                        '{.tmp/,}src/main/webapp/assets/styles/**/*.css',
		                        '{.tmp/,}src/main/webapp/scripts/**/*.js',
		                        'src/main/webapp/assets/images/**/*.{png,jpg,jpeg,gif,webp,svg}'
		                    ]
		                }
		            },
		            options: {
		                watchTask: true,
		                proxy: "localhost:8082"
		            }
		        },
		        clean: {
		            dist: {
		                files: [{
		                    dot: true,
		                    src: [
		                        '.tmp',
		                        '<%= yeoman.dist %>/*',
		                        '!<%= yeoman.dist %>/.git*'
		                    ]
		                }]
		            },
		            server: '.tmp'
		        },
		        
		        jshint: {
		            options: {
		                jshintrc: '.jshintrc'
		            },
		            all: [
		                'Gruntfile.js',
		                'src/main/webapp/scripts/yacra.js',
		                'src/main/webapp/scripts/app/**/*.js'
		            ]
		        },
				 coffee: {
			            options: {
			                sourceMap: true,
			                sourceRoot: ''
			            },
			            dist: {
			                files: [{
			                    expand: true,
			                    cwd: 'src/main/webapp/scripts',
			                    src: ['scripts/app/**/*.coffee', 'scripts/components/**/*.coffee'],
			                    dest: '.tmp/scripts',
			                    ext: '.js'
			                }]
			            },
			            test: {
			                files: [{
			                    expand: true,
			                    cwd: 'test/spec',
			                    src: '**/*.coffee',
			                    dest: '.tmp/spec',
			                    ext: '.js'
			                }]
			            }
			     },
			     concat: {
			            // not used since Uglify task does concat,
			            // but still available if needed
			            //    dist: {}
			    },
	            rev: {
	                dist: {
	                    files: {
	                        src: [
	                            '<%= yeoman.dist %>/scripts/**/*.js',
	                            '<%= yeoman.dist %>/assets/styles/**/*.css',
	                            '<%= yeoman.dist %>/assets/images/**/*.{png,jpg,jpeg,gif,webp,svg}',
	                            '<%= yeoman.dist %>/assets/fonts/**/*'
	                        ]
	                    }
	                }
	            },       
	            useminPrepare: {
	                html: 'src/main/webapp/**/*.html',
	                options: {
	                    dest: '<%= yeoman.dist %>',
	                    flow: {
	                        html: {
	                            steps: {
	                                js: ['concat', 'uglifyjs'],
	                                css: ['cssmin', useminAutoprefixer] // Let cssmin concat files so it corrects relative paths to fonts and images
	                            },
	                                post: {}
	                            }
	                        }
	                }
	            },
	            usemin: {
	                html: ['<%= yeoman.dist %>/**/*.html'],
	                css: ['<%= yeoman.dist %>/assets/styles/**/*.css'],
	                js: ['<%= yeoman.dist %>/scripts/**/*.js'],
	                options: {
	                    assetsDirs: ['<%= yeoman.dist %>', '<%= yeoman.dist %>/assets/styles', '<%= yeoman.dist %>/assets/images', '<%= yeoman.dist %>/assets/fonts'],
	                    patterns: {
	                        js: [
	                            [/(assets\/images\/.*?\.(?:gif|jpeg|jpg|png|webp|svg))/gm, 'Update the JS to reference our revved images']
	                        ]
	                    }
//	            ,
//	                    dirs: ['<%= yeoman.dist %>']
	                }
	            },
	            imagemin: {
	                dist: {
	                    files: [{
	                        expand: true,
	                        cwd: 'src/main/webapp/assets/images',
	                    src: '**/*.{jpg,jpeg}', // we don't optimize PNG files as it doesn't work on Linux. If you are not on Linux, feel free to use '**/*.{png,jpg,jpeg}'
	                        dest: '<%= yeoman.dist %>/assets/images'
	                    }]
	                }
	            },
	            svgmin: {
	                dist: {
	                    files: [{
	                        expand: true,
	                        cwd: 'src/main/webapp/assets/images',
	                        src: '**/*.svg',
	                        dest: '<%= yeoman.dist %>/assets/images'
	                    }]
	                }
	            },
	            cssmin: {
	                // By default, your `index.html` <!-- Usemin Block --> will take care of
	                // minification. This option is pre-configured if you do not wish to use
	                // Usemin blocks.
	                // dist: {
	                //     files: {
	                //         '<%= yeoman.dist %>/styles/main.css': [
	                //             '.tmp/styles/**/*.css',
	                //             'styles/**/*.css'
	                //         ]
	                //     }
	                // }
	                options: {
	                    root: 'src/main/webapp' // Replace relative paths for static resources with absolute path
	                }
	            },
	            ngtemplates:    {
	                dist: {
	                    cwd: 'src/main/webapp',
	                    src: ['scripts/app/**/*.html','scripts/components/**/*.html','scripts/templates/**/*.html'],
	                    dest: '.tmp/templates/templates.js',
	                    options: {
	                        module: 'yaCRAApp',
	                        usemin: 'scripts/app/app.js',
	                        htmlmin:  {
	                            removeCommentsFromCDATA: true,
	                            // https://github.com/yeoman/grunt-usemin/issues/44
	                            collapseWhitespace: true,
	                            collapseBooleanAttributes: true,
	                            conservativeCollapse: true,
	                            removeAttributeQuotes: true,
	                            removeRedundantAttributes: true,
	                            useShortDoctype: true,
	                            removeEmptyAttributes: true
	                        }
	                    }
	                }
	            },
	            htmlmin: {
	                dist: {
	                    options: {
	                        removeCommentsFromCDATA: true,
	                        // https://github.com/yeoman/grunt-usemin/issues/44
	                        collapseWhitespace: true,
	                        collapseBooleanAttributes: true,
	                        conservativeCollapse: true,
	                        removeAttributeQuotes: true,
	                        removeRedundantAttributes: true,
	                        useShortDoctype: true,
	                        removeEmptyAttributes: true,
	                        keepClosingSlash: true
	                    },
	                  /*  files:{                                   // Dictionary of files
	                        '<%= yeoman.dist %>/index.html': 'src/main/webapp/views/index.html',     // 'destination': 'source'
	                        '<%= yeoman.dist %>/views/app/index.html': 'src/main/webapp/views/app/index.html',
	                        '<%= yeoman.dist %>/views/auth/login.html': 'src/main/webapp/views/auth/login.html',
	                        '<%= yeoman.dist %>/views/auth/register.html': 'src/main/webapp/views/auth/register.html',
	                        '<%= yeoman.dist %>/views/auth/forgot-password.html': 'src/main/webapp/views/auth/forgot-password.html'
	                      }*/
	                    files: [{
	                        expand: true,
	                        cwd: '<%= yeoman.dist %>',
	                        src: ['views/**/*.html'],
	                        dest: '<%= yeoman.dist %>'
	                    }]
	                }
	            },
	         // Put files not handled in other tasks here
	            copy: {
	                dist: {
	                    files: [{
	                        expand: true,
	                        dot: true,
	                        cwd: 'src/main/webapp',
	                        dest: '<%= yeoman.dist %>',
	                        src: [
	                            'views/**/*.html',
	                            'scripts/**/*.html',
	                            'assets/images/**/*.{png,gif,webp,jpg,jpeg,svg}',
	                            'assets/fonts/**/*'
	                        ]
	                    }, {
	                        expand: true,
	                        cwd: '.tmp/assets/images',
	                        dest: '<%= yeoman.dist %>/assets/images',
	                        src: [
	                            'generated/*'
	                        ]
	                    }]
	                },
	                generateHerokuDirectory: {
	                        expand: true,
	                        dest: 'deploy/heroku',
	                        src: [
	                            'pom.xml',
	                            'src/main/**'
	                    ]
	                },
	                generateOpenshiftDirectory: {
	                        expand: true,
	                        dest: 'deploy/openshift',
	                        src: [
	                            'pom.xml',
	                            'src/main/**'
	                    ]
	                }
	            },
	            concurrent: {
	                server: [
	                ],
	                test: [
	                ],
	                dist: [
	                    'imagemin',
	                    'svgmin'
	                ]
	            },
	            cdnify: {
	                dist: {
	                    html: ['<%= yeoman.dist %>/*.html']
	                }
	            },
	            ngAnnotate: {
	                dist: {
	                    files: [{
	                        expand: true,
	                        cwd: '.tmp/concat/scripts',
	                        src: '**/*.js',
	                        dest: '.tmp/concat/scripts'
	                    }]
	                }
	            },
	            buildcontrol: {
	                options: {
	                    commit: true,
	                    push: false,
	                    connectCommits: false,
	                    message: 'Built %sourceName% from commit %sourceCommit% on branch %sourceBranch%'
	                },
	                heroku: {
	                    options: {
	                        dir: 'deploy/heroku',
	                        remote: 'heroku',
	                        branch: 'master'
	                    }
	                },
	                openshift: {
	                    options: {
	                        dir: 'deploy/openshift',
	                        remote: 'openshift',
	                        branch: 'master'
	                    }
	                }
	            },
	            ngconstant: {
	                options: {
	                    name: 'yaCRAApp',
	                    deps: false,
	                    wrap: '"use strict";\n// DO NOT EDIT THIS FILE, EDIT THE GRUNT TASK NGCONSTANT SETTINGS INSTEAD WHICH GENERATES THIS FILE\n{%= __ngModule %}'
	                },
	                dev: {
	                    options: {
	                        dest: 'src/main/webapp/scripts/app/app.constants.js',
	                    },
	                    constants: {
	                        ENV: 'dev',
	                        VERSION: parseVersionFromPomXml()
	                    }
	                },
	                prod: {
	                    options: {
	                        dest: '.tmp/scripts/app/app.constants.js',
	                    },
	                    constants: {
	                        ENV: 'prod',
	                        VERSION: parseVersionFromPomXml()
	                    }
	                }
	            }
		    });
	grunt.registerTask('serve', [
	                             'clean:server',
	                             'wiredep',
	                             'ngconstant:dev',
	                             'concurrent:server',
	                             'browserSync',
	                             'watch'
	                         ]);

	                         grunt.registerTask('server', function (target) {
	                             grunt.log.warn('The `server` task has been deprecated. Use `grunt serve` to start a server.');
	                             grunt.task.run([target ? ('serve:' + target) : 'serve']);
	                         });

	                         grunt.registerTask('test', [
	                             'clean:server',
	                             'wiredep:test',
	                             'ngconstant:dev',
	                             'concurrent:test',
	                             'karma'
	                         ]);

	                         grunt.registerTask('build', [
	                             'clean:dist',
//	                             'wiredep:app',
	                             'ngconstant:prod',
	                             'useminPrepare',
	                             'ngtemplates',
	                             'concurrent:dist',
	                             'concat',
	                             'copy:dist',
	                             'ngAnnotate',
	                             'cssmin',
	                             'autoprefixer',
	                             'uglify',
	                             'rev',
	                             'usemin',
	                             'htmlmin'
	                         ]);

	                         grunt.registerTask('buildHeroku', [
	                             'test',
	                             'build',
	                             'copy:generateHerokuDirectory',
	                         ]);

	                         grunt.registerTask('deployHeroku', [
	                             'test',
	                             'build',
	                             'copy:generateHerokuDirectory',
	                             'buildcontrol:heroku'
	                         ]);

	                         grunt.registerTask('buildOpenshift', [
	                             'test',
	                             'build',
	                             'copy:generateOpenshiftDirectory',
	                         ]);

	                         grunt.registerTask('deployOpenshift', [
	                             'test',
	                             'build',
	                             'copy:generateOpenshiftDirectory',
	                             'buildcontrol:openshift'
	                         ]);

	                         grunt.registerTask('default', [
	                             'test',
	                             'build'
	                         ]);
};