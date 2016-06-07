JavaNCSS - Source Measurement Suite for Java
============================================

[![Build Status](https://secure.travis-ci.org/JavaNCSS/javancss.svg)](http://travis-ci.org/JavaNCSS/javancss)
[![Coverage Status](https://coveralls.io/repos/github/JavaNCSS/javancss/badge.svg?branch=master)](https://coveralls.io/github/JavaNCSS/javancss?branch=master)
[![License](https://img.shields.io/badge/license-LGPL--3.0+-blue.svg)](http://www.gnu.org/licenses/lgpl-3.0.en.html)
[![Maven Central](https://img.shields.io/maven-central/v/org.codehaus.javancss/javancss.svg?maxAge=2592000)](http://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22org.codehaus.javancss%22%20AND%20a%3A%22javancss%22)


Ever wondered how many lines of code or how many classes there are in the JDK? Curious about the size of your own 
projects - or do you want to keep track of your work-progress. That's what JavaNCSS is for.

JavaNCSS is a simple command line utility which measures two standard source code metrics for the Java programming
language. The metrics are collected globally, for each class and/or for each function.

Features
========

 * Metrics can be applied to global-, class-, or function-level.
 * Non Commenting Source Statements (NCSS).
 * Cyclomatic Complexity Number (McCabe metric).
 * Packages, classes, functions and inner classes are counted.
 * Number of formal Javadoc comments per class and method.
 * Average values are calculated.
 * Command line driven.
 * A GUI is provided for better output browsing as well as the integration of JavaNCSS in a project manager and class
   browser named Jacob (http://www.kclee.de/clemens/jacob/) for easy selection of input source files.
 * XML output (optional).
 * XSLT stylesheets provided for further generation of HTML, and SVG reports and as a basis for your own custom reports.
 * Ant task (written by Steve Jernigan).
 * Support for Java 5 syntax (generics, enums, etc.).
 * Support for Java 7 syntax (try with resource, literals, etc.).
 * 100% Pure Java.
 * Free software (GNU LGPL).

System Requirements
===================

   Java JDK >= 5
   
Installation
============

   See http://javancss.codehaus.org/

Version
=======

   See http://javancss.codehaus.org/release-history.html

Date
====

   See http://javancss.codehaus.org/release-history.html

URL
===
   http://javancss.codehaus.org/
   (was http://www.kclee.de/clemens/java/javancss/)

Authors
=======

   Chr. Clemens Lee, <clemens@kclee.de>
   http://xircles.codehaus.org/projects/javancss/members 
