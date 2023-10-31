<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:loc="http://it.nure.ua/xml/entity/location/"
                xmlns:pl="http://it.nure.ua/xml/entity/place/"
                xmlns:ent="http://it.nure.ua/xml/entity/">

    <!-- Основной стиль для страницы -->
    <xsl:template match="/">
        <html>
            <head>
                <style>
                    body {
                    font-family: Arial, sans-serif;
                    background-color: #f4f4f4;
                    margin: 20px;
                    }
                    h1, h2 {
                    color: #333366;
                    border-bottom: 2px solid #333366;
                    padding-bottom: 10px;
                    }
                    ul {
                    list-style-type: none;
                    padding: 0;
                    }
                    li {
                    background-color: #fff;
                    padding: 10px;
                    margin-bottom: 10px;
                    border-radius: 5px;
                    box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
                    }
                    img {
                    margin-right: 10px;
                    border-radius: 5px;
                    }
                    span {
                    background-color: #ddd;
                    padding: 2px 5px;
                    margin-right: 5px;
                    border-radius: 3px;
                    }

                    li > div.place {
                    display: flex;
                    flex-direction: column;
                    gap: 10px;
                    padding: 10px;
                    border: 1px solid #ddd;
                    border-radius: 5px;
                    margin-top: 10px;
                    }
                    li > div.place > strong {
                    color: #555;
                    margin-right: 5px;
                    }
                    li > div.place > img {
                    max-width: 150px;
                    border: 1px solid #ddd;
                    }
                    li > div.place > span.tag {
                    background-color: #eee;
                    padding: 2px 8px;
                    border-radius: 5px;
                    margin-right: 5px;
                    display: inline-block;
                    }

                    .container {
                    max-width: 1200px;
                    margin: 0 auto;
                    padding: 0 20px;
                    }

                    /* Стили для элементов place */
                    li > div.place {
                    /* ... (предыдущие стили) ... */
                    margin-bottom: 20px;
                    }

                    /* Адаптивные стили */
                    @media (max-width: 768px) {
                    .container {
                    padding: 0 10px;
                    }

                    li > div.place {
                    flex-direction: column;
                    }

                    h1 .main-text {
                    text-align: center;
                    margin-top: 50px;
                    }

                    .recommended {
                    color: green;
                    font-weight: bold;
                    border: 1px solid green;
                    display: inline-block;
                    padding: 2px 5px;
                    border-radius: 5px;
                    margin-top: 10px;
                    }
                    .photos {
                    display: flex;
                    gap: 10px;
                    margin-top: 10px;
                    }
                </style>
            </head>
            <body>
                <div class="container">
                    <xsl:apply-templates select="loc:locations"/>
                </div>
            </body>
        </html>
    </xsl:template>

    <!-- Шаблон для корневого элемента -->
    <xsl:template match="loc:locations">
        <h1 class="main-text">Локаціі</h1>
        <xsl:apply-templates select="loc:location"/>
    </xsl:template>

    <!-- Шаблон для элемента location -->
    <xsl:template match="loc:location">
        <h2>
            <xsl:value-of select="loc:title"/>
        </h2>
        <p>Координати:</p>
        <ul>
            <xsl:for-each select="loc:area/loc:coordinate">
                <li>
                    <xsl:value-of select="pl:Latitude"/>,&#160;<xsl:value-of select="pl:Longitude"/>
                </li>
            </xsl:for-each>
        </ul>
        <p>Місця:</p>
        <ul>
            <xsl:apply-templates select="loc:places/loc:place"/>
        </ul>
    </xsl:template>

    <!-- Шаблон для элемента place -->
    <xsl:template match="loc:place">
        <li>
            <div class="place">
                <strong>Назва:</strong>
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
