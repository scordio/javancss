<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0"
    xmlns:java="http://xml.apache.org/xslt/java"
    exclude-result-prefixes="java">
 
  <xsl:output method="text"/>

  <xsl:template name="pad">
    <xsl:param name="value"/>
    <xsl:param name="length"/>
    <xsl:choose>
      <xsl:when test="string-length($value) &lt; $length">
        <xsl:call-template name="pad">
          <xsl:with-param name="value" select="concat(' ',$value)"/>
          <xsl:with-param name="length" select="$length"/>
        </xsl:call-template>
      </xsl:when>
      <xsl:otherwise>
        <xsl:value-of select="$value"/>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>
    
  <xsl:template match="packages">
    <xsl:call-template name="pad"><xsl:with-param name="length" select="java:java.lang.Math.max(string-length(count(package)),3)"/><xsl:with-param name="value" select="'Nr.'"/></xsl:call-template>
    <xsl:text>   Classes Functions      NCSS  Javadocs Package
</xsl:text>

    <xsl:apply-templates select="package"/>

    <xsl:call-template name="pad"><xsl:with-param name="length" select="java:java.lang.Math.max(string-length(count(package)),3)"/><xsl:with-param name="value" select="'   '"/></xsl:call-template>
    <xsl:text> --------- --------- --------- ---------
</xsl:text>

    <xsl:call-template name="pad"><xsl:with-param name="length" select="java:java.lang.Math.max(string-length(count(package)),4)"/><xsl:with-param name="value" select="'   '"/></xsl:call-template>

    <xsl:call-template name="pad"><xsl:with-param name="length" select="9"/><xsl:with-param name="value" select="total/classes"/></xsl:call-template>
    <xsl:text> </xsl:text>
    <xsl:call-template name="pad"><xsl:with-param name="length" select="9"/><xsl:with-param name="value" select="total/functions"/></xsl:call-template>
    <xsl:text> </xsl:text>
    <xsl:call-template name="pad"><xsl:with-param name="length" select="9"/><xsl:with-param name="value" select="total/ncss"/></xsl:call-template>
    <xsl:text> </xsl:text>
    <xsl:call-template name="pad"><xsl:with-param name="length" select="9"/><xsl:with-param name="value" select="total/javadocs"/></xsl:call-template>
    <xsl:text> Total

</xsl:text>

    <xsl:apply-templates select="table"/>

    <xsl:text>
</xsl:text>


  </xsl:template>

  <xsl:template match="package">
    <xsl:call-template name="pad"><xsl:with-param name="length" select="java:java.lang.Math.max(string-length(count(../package)),3)"/><xsl:with-param name="value" select="substring-before(java:java.lang.String.valueOf(position()),'.0')"/></xsl:call-template>
    <xsl:text> </xsl:text>
    <xsl:call-template name="pad"><xsl:with-param name="length" select="9"/><xsl:with-param name="value" select="classes"/></xsl:call-template>
    <xsl:text> </xsl:text>
    <xsl:call-template name="pad"><xsl:with-param name="length" select="9"/><xsl:with-param name="value" select="functions"/></xsl:call-template>
    <xsl:text> </xsl:text>
    <xsl:call-template name="pad"><xsl:with-param name="length" select="9"/><xsl:with-param name="value" select="ncss"/></xsl:call-template>
    <xsl:text> </xsl:text>
    <xsl:call-template name="pad"><xsl:with-param name="length" select="9"/><xsl:with-param name="value" select="javadocs"/></xsl:call-template>
    <xsl:text> </xsl:text>
    <xsl:value-of select="name"/>
    <xsl:text>
</xsl:text>
  </xsl:template>

  <xsl:template match="table">
    <xsl:apply-templates select="tr[position()=1]"/>
    <xsl:text>-------------------------------------------------------------
</xsl:text>
    <xsl:apply-templates select="tr[position()!=1]"/>
  </xsl:template>

  <xsl:template match="tr">
    <xsl:apply-templates select="td[position()!=6]"/>
    <xsl:text>| </xsl:text>
    <xsl:value-of select="td[position()=6]"/>
    <xsl:text>
</xsl:text>
  </xsl:template>

  <xsl:template match="td">
    <xsl:call-template name="pad"><xsl:with-param name="length" select="9"/><xsl:with-param name="value" select="."/></xsl:call-template>
    <xsl:text> </xsl:text>
  </xsl:template>

  <xsl:template match="objects">
    <xsl:call-template name="pad"><xsl:with-param name="length" select="java:java.lang.Math.max(string-length(count(object)),3)"/><xsl:with-param name="value" select="'Nr.'"/></xsl:call-template>
    <xsl:text> NCSS Functions Classes Javadocs Class
</xsl:text>
    <xsl:apply-templates select="object"/>
    <xsl:text>Average Object NCSS:             </xsl:text>
    <xsl:call-template name="pad"><xsl:with-param name="length" select="9"/><xsl:with-param name="value" select="averages/ncss"/></xsl:call-template>
    <xsl:text>
Average Object Functions:        </xsl:text>
    <xsl:call-template name="pad"><xsl:with-param name="length" select="9"/><xsl:with-param name="value" select="averages/functions"/></xsl:call-template>
    <xsl:text>
Average Object Inner Classes:    </xsl:text>
    <xsl:call-template name="pad"><xsl:with-param name="length" select="9"/><xsl:with-param name="value" select="averages/classes"/></xsl:call-template>
    <xsl:text>
Average Object Javadoc Comments: </xsl:text>
    <xsl:call-template name="pad"><xsl:with-param name="length" select="9"/><xsl:with-param name="value" select="averages/javadocs"/></xsl:call-template>
    <xsl:text>
Program NCSS:                    </xsl:text>
    <xsl:call-template name="pad"><xsl:with-param name="length" select="9"/><xsl:with-param name="value" select="ncss"/></xsl:call-template>
    <xsl:text>

</xsl:text>
  </xsl:template>

  <xsl:template match="object">
    <xsl:call-template name="pad"><xsl:with-param name="length" select="java:java.lang.Math.max(string-length(count(../object)),3)"/><xsl:with-param name="value" select="substring-before(java:java.lang.String.valueOf(position()),'.0')"/></xsl:call-template>
    <xsl:text> </xsl:text>
    <xsl:call-template name="pad"><xsl:with-param name="length" select="4"/><xsl:with-param name="value" select="ncss"/></xsl:call-template>
    <xsl:text> </xsl:text>
    <xsl:call-template name="pad"><xsl:with-param name="length" select="9"/><xsl:with-param name="value" select="functions"/></xsl:call-template>
    <xsl:text> </xsl:text>
    <xsl:call-template name="pad"><xsl:with-param name="length" select="7"/><xsl:with-param name="value" select="classes"/></xsl:call-template>
    <xsl:text> </xsl:text>
    <xsl:call-template name="pad"><xsl:with-param name="length" select="8"/><xsl:with-param name="value" select="javadocs"/></xsl:call-template>
    <xsl:text> </xsl:text>
    <xsl:value-of select="name"/>
    <xsl:text>
</xsl:text>    
  </xsl:template>

  <xsl:template match="functions">
    <xsl:call-template name="pad"><xsl:with-param name="length" select="java:java.lang.Math.max(string-length(count(function)),3)"/><xsl:with-param name="value" select="'Nr.'"/></xsl:call-template>
    <xsl:text> NCSS CCN JVDC Function
</xsl:text>
    <xsl:apply-templates select="function"/>
    <xsl:text>Average Function NCSS: </xsl:text>
    <xsl:call-template name="pad"><xsl:with-param name="length" select="10"/><xsl:with-param name="value" select="function_averages/ncss"/></xsl:call-template>
    <xsl:text>
Average Function CCN:  </xsl:text>
    <xsl:call-template name="pad"><xsl:with-param name="length" select="10"/><xsl:with-param name="value" select="function_averages/ccn"/></xsl:call-template>
    <xsl:text>
Average Function JVDC: </xsl:text>
    <xsl:call-template name="pad"><xsl:with-param name="length" select="10"/><xsl:with-param name="value" select="function_averages/javadocs"/></xsl:call-template>
    <xsl:text>
Program NCSS:          </xsl:text>
    <xsl:call-template name="pad"><xsl:with-param name="length" select="10"/><xsl:with-param name="value" select="ncss"/></xsl:call-template>
    <xsl:text>
</xsl:text>
  </xsl:template>

  <xsl:template match="function">
    <xsl:call-template name="pad"><xsl:with-param name="length" select="java:java.lang.Math.max(string-length(count(../function)),3)"/><xsl:with-param name="value" select="substring-before(java:java.lang.String.valueOf(position()),'.0')"/></xsl:call-template>
    <xsl:text> </xsl:text>
    <xsl:call-template name="pad"><xsl:with-param name="length" select="4"/><xsl:with-param name="value" select="ncss"/></xsl:call-template>
    <xsl:text> </xsl:text>
    <xsl:call-template name="pad"><xsl:with-param name="length" select="3"/><xsl:with-param name="value" select="ccn"/></xsl:call-template>
    <xsl:text> </xsl:text>
    <xsl:call-template name="pad"><xsl:with-param name="length" select="4"/><xsl:with-param name="value" select="javadocs"/></xsl:call-template>
    <xsl:text> </xsl:text>
    <xsl:value-of select="name"/>
    <xsl:text>
</xsl:text>    
  </xsl:template>

  <xsl:template match="text()"/>

</xsl:stylesheet>
