<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:loc="http://it.nure.ua/xml/entity/location/"
                xmlns:pl="http://it.nure.ua/xml/entity/place/">

    <xsl:include href="place.xsl"/>
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

</xsl:stylesheet>
