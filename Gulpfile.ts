/// <reference path="typings/tsd.d.ts"/>

import gulp = require('gulp');
import ts = require('gulp-typescript');

gulp.task('typescript', () => {
    return gulp.src('./src/**/*.ts').pipe(ts({
        module: 'commonjs'
    })).pipe(gulp.dest('./'));
});

gulp.task('install', ['typescript']);
