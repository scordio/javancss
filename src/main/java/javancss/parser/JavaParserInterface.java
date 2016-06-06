package javancss.parser;

import java.util.List;
import java.util.Map;

import javancss.FunctionMetric;
import javancss.ObjectMetric;
import javancss.PackageMetric;

public interface JavaParserInterface
{
    void parse() throws Exception;

    void parseImportUnit() throws Exception;

    int getNcss();

    int getLOC();

    // added by SMS
    int getJvdc();

    /*public int getTopLevelClasses() {
      return _topLevelClasses;
      }*/

    List<FunctionMetric> getFunction();

    /**
     * @return Top level classes in sorted order
     */
    List<ObjectMetric> getObject();

    /**
     * @return The empty package consists of the name ".".
     */
    Map<String, PackageMetric> getPackage();

    List<Object[]> getImports();

    /**
     * name, beginLine, ...
     */
    Object[] getPackageObjects();

    /**
     * if javancss is used with cat *.java a long
     * input stream might get generated, so line
     * number information in case of an parse exception
     * is not very useful.
     */
    String getLastFunction();
}
