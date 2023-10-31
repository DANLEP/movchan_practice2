<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:loc="http://it.nure.ua/xml/entity/location/"
                xmlns:pl="http://it.nure.ua/xml/entity/place/">
    <!-- Шаблон для элемента place -->
<xsl:template match="loc:place">
    <li>
        <div class="place">
            <strong><xsl:value-of select="position()"/></strong>
            <xsl:value-of select="pl:title"/>
            <br/>
            <strong>Опис:</strong>
            <xsl:value-of select="pl:description"/>
            <br/>
            <strong>Тип:</strong>
            <xsl:value-of select="pl:type"/>
            <br/>

            <xsl:if test="pl:address">
                <strong>Адрес:</strong>
                <xsl:value-of select="pl:address/pl:Street"/>&#160;<xsl:choose>
                <xsl:when test="pl:address/pl:HouseNumber/pl:Number">
                    <xsl:value-of select="pl:address/pl:HouseNumber/pl:Number"/>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:value-of select="pl:address/pl:HouseNumber/pl:NumberWithLetter"/>
                </xsl:otherwise>
            </xsl:choose>
                ,
                <xsl:value-of select="pl:address/pl:City"/>,
                <xsl:value-of select="pl:address/pl:PostalCode"/>
                <br/>
            </xsl:if>

            <strong>Координати:</strong>
            <xsl:value-of select="pl:coordinate/pl:Latitude"/>,&#160;<xsl:value-of
                select="pl:coordinate/pl:Longitude"/>
            <br/>

            <xsl:if test="pl:visitTime">
                <strong>Час відвідування:</strong>
                <xsl:value-of select="pl:visitTime"/>
                <br/>
            </xsl:if>

            <xsl:if test="pl:entranceFee">
                <strong>Вартість входу:</strong>
                Вартість:&#160;<xsl:value-of select="pl:entranceFee/pl:Price"/>,
                Валюта:&#160;<xsl:value-of select="pl:entranceFee/pl:Currency"/>
                <br/>
            </xsl:if>

            <xsl:if test="pl:photos">
                <strong>Фото:</strong>
                <!-- Блок фотографій -->
                <div class="photos">
                    <xsl:for-each select="pl:photos/pl:photo">
                        <img src="{.}" alt="Фото місця" width="100"/>
                    </xsl:for-each>
                </div>
                <br/>
            </xsl:if>

            <strong>Созонність:</strong>
            <xsl:value-of select="pl:seasonality"/>
            <br/>

            <xsl:if test="pl:tags">
                <strong>Теги:</strong>
                <xsl:for-each select="pl:tags/pl:Tag">
                    <xsl:sort select="." order="descending"/>
<!--               ascending or descending     -->
                    <span>
                        <xsl:value-of select="."/>
                    </span>
                </xsl:for-each>
                <br/>
            </xsl:if>

            <xsl:choose>
                <xsl:when test="@isRecommended='true'">
                    <span class="recommended">Рекомендовано</span>
                </xsl:when>
            </xsl:choose>

            <xsl:if test="@rating">
                <strong>Рейтинг:</strong>
                <xsl:value-of select="@rating"/>
            </xsl:if>
        </div>
    </li>
</xsl:template>
</xsl:stylesheet>