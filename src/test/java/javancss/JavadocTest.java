/*
Copyright (C) 2000 Chr. Clemens Lee <clemens@kclee.com>.

This file is part of JavaNCSS
(http://www.kclee.de/clemens/java/javancss/).

JavaNCSS is free software; you can redistribute it and/or modify it
under the terms of the GNU General Public License as published by the
Free Software Foundation; either version 2, or (at your option) any
later version.

JavaNCSS is distributed in the hope that it will be useful, but WITHOUT
ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
for more details.

You should have received a copy of the GNU General Public License
along with JavaNCSS; see the file COPYING.  If not, write to
the Free Software Foundation, Inc., 59 Temple Place - Suite 330,
Boston, MA 02111-1307, USA.  */

package javancss;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class JavadocTest extends AbstractTestCase
{
   /**
    * There has been a bug introduced for version 16.34 which
    * counts Javadoc comments (**) for fields as well as for
    * methods, while I am only in the later ones.
    * File Test20 has 6 methods and 6 + 1 ** comments.
    * This test should make sure that 3 attribute comments
    * won't be counted.
    */
   public void testJavadocs()
   {
       checkJvdcs( 20, 7 );
       checkJvdcs( 46, 3 );
       checkJvdcs( 47, 2 );
       checkJvdcs( 68, 2 );
       checkJvdcs( 121, 2 );
       checkJvdcs( 122, 1 );
       checkJvdcs( 139, 3 ); // JAVANCSS-20
       checkJvdcs( 140, 2 ); // JAVANCSS-20
       checkJvdcs( 141, 1 ); // JAVANCSS-20
   }

   /**
    * Check that Javadoc line counts are correct.
    * There is one bug where there are only two files with
    * a package jacob in the test directory (Test1.java and
    * Test28.java), and while both have no javadocs at all,
    * the count is still 11. The eleven seem to come from
    * files Test20.java and Test21.java.
    * This test shall trace this bug down and shall later asure
    * that it got fixed.
    */
   public void testJavadocLines()
   {
       checkJavadocLines( 28, "jacob", 0 );

       //
       // same test with more files
       //
       checkJavadocLines( new int[]{ 20, 21, 28 }, "jacob", 0 );

       checkJavadocLines( 68, ".", 6 );
       checkJavadocLines( 69, ".", 4 );
       checkJavadocLines( new int[]{ 68, 69 }, ".", 10 );
       checkJavadocLines( 65, "idebughc.testsuite", 14 );
   }

   private void checkJavadocLines( int testFile, String sPackage, int javadocLines )
   {
       Javancss pJavancss = measureTestFile( testFile );

       checkJavadocLines( pJavancss, sPackage, javadocLines );
   }

   private void checkJavadocLines( int[] aTestFile, String sPackage, int javadocLines )
   {
       List<File> files = new ArrayList<File>();
       for( int next : aTestFile )
       {
           files.add( getTestFile( next ) );
       }

       Javancss pJavancss = new Javancss( files );
       checkJavadocLines( pJavancss, sPackage, javadocLines );
   }

   private void checkJavadocLines( Javancss pJavancss, String sPackage, int javadocLines )
   {
       List<PackageMetric> vPackageMetrics = pJavancss.getPackageMetrics();
       assertTrue( vPackageMetrics.size() >= 1 );
       PackageMetric pmPackage = null;
       for ( PackageMetric pmNext : vPackageMetrics )
       {
           if ( pmNext.name.equals( sPackage ) )
           {
               pmPackage = pmNext;
           }
       }
       assertNotNull( pmPackage );
       assertEquals( "pmJacob.javadocsLn: " + pmPackage + ": " + pmPackage.javadocsLn,
               javadocLines, pmPackage.javadocsLn );
   }

   private void checkJvdcs( int testFileNumber, int expectedJvdcsResult )
   {
       Javancss javancss = measureTestFile( testFileNumber );
       List<ObjectMetric> vObjectMetrics = javancss.getObjectMetrics();
       ObjectMetric classMetric = vObjectMetrics.get( 0 );
       int jvdcs = classMetric.javadocs;
       /* int jvdc = javancss.getJvdc(); */
       assertEquals( "Parsing file Test" + testFileNumber + ".java failed. Jvdc is " + jvdcs + " and not " + expectedJvdcsResult + ".",
               expectedJvdcsResult, jvdcs );
   }
}
