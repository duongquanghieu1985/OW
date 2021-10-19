<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:template match="/">
		<html >
			<head>
				
				<script type="text/javascript" src="jquery.min.js"></script>
				<script type="text/javascript" src="ddtf.js"></script>
				<script type="text/javascript">
					$(document).ready(function(){
					$('#summary').ddTableFilter();
					$('#details').ddTableFilter();
					});
				</script>
				<style type="text/css">
					a:link {
					color: #8ac007;
					text-decoration: none;
					}
					a:visited {
					color: #8ac007;
					}
					a:hover {
					color: #215967;
					text-decoration: underline;
					}
					a:active {
					color: #8ac007;
					}
					#header {
					border-radius: 10px 10px 0 0;
					padding: 10px;
					background: #00b050;
					width: 99%;
					font-size: 11px;
					color: white;
					font-weight: bold;
					text-align: center;
					}
					#dashboard {
					border-collapse: collapse;
					border-radius: 10px 10px 0 0;
					padding: 10px;
					margin: 10px;
					min-width: 700px;
					box-shadow: 0 0 20px rgba(0, 0, 0, 0.15);
					text-align: center;
					font-size: 12px;
					}
					#summary {
					border-collapse:
					collapse;
					border-radius: 10px 10px 0 0;
					padding: 10px;
					margin: 10px;
					font-size: 12px;
					min-width: 700px;
					box-shadow: 0 0 20px rgba(0, 0, 0, 0.15);
					text-align: center;
					padding: 10px;
					}
					#details {
					border-collapse:
					collapse;
					border-radius: 10px 10px 0 0;
					padding: 10px;
					margin: 10px;
					font-size: 12px;
					min-width: 700px;
					box-shadow: 0 0 20px rgba(0, 0, 0, 0.15);
					padding: 10px;
					border: thin;
					}
				</style>
			</head>
			<body style="font-family: Arial;">
				<div id="header">TEST SUMMARY</div>
				<br style="line-height: 5px;" />
				<table id="dashboard" border="1" cellpadding="3"
					style="border-collapse: collapse;table-layout:fixed;min-width: 700px;">
					<tr bgcolor="#00b050" style="color:white;font-weight: bold;">
						<td style="text-align: center;">Start Time</td>
						<td style="text-align: center;">End Time</td>
						<td style="text-align: center;">Duration</td>
						<td style="text-align: center;"># Pass Tests</td>
						<td style="text-align: center;"># Failed Tests</td>
						<td style="text-align: center;"># Warning Tests</td>
						<td style="text-align: center;">Total Tests</td>
					</tr>
					<tr style="text-align: center;">
						<td>
							<xsl:value-of select="TestResult/Summary/StartTime" />
						</td>
						<td>
							<xsl:value-of select="TestResult/Summary/EndTime" />
						</td>
						<td>
							<xsl:value-of select="TestResult/Summary/Duration" />
						</td>
						<td style="color:green;font-weight:bold;">
							<xsl:value-of
								select="count(TestResult/TestScenario[@Result = 'Passed'])" />
						</td>
						<td style="color:red;font-weight:bold;">
							<xsl:value-of
								select="count(TestResult/TestScenario[@Result = 'Failed'])" />
						</td>
						<td style="color:orange;font-weight:bold;">
							<xsl:value-of
								select="count(TestResult/TestScenario[@Result = 'Warning'])" />
						</td>
						<td style="font-weight:bold;">
							<xsl:value-of
								select="count(TestResult/TestScenario)" />
						</td>
					</tr>
				</table>
				<br style="line-height: 5px;" />
				<table
					style="border-collapse: collapse; table-layout: fixed; min-width: 700px;"
					border="1" id="summary">
					<tr style="text-align:center;color:white;" bgcolor="#00b050">
						<th>#</th>
						<th>Test Name</th>
						<th>Steps</th>
						<th>Status</th>
						<th>Folder stores files created by Script</th>
						<th>Duration</th>
					</tr>
					<xsl:for-each select="TestResult/TestScenario">
						<tr>
							<td>
								<xsl:value-of select="position()" />
							</td>
							<td>
								<a href="#{position()}">
									<xsl:value-of select="./@Title" />
								</a>
							</td>
							<td>
								<xsl:value-of select="./@NStep" />
							</td>

							<xsl:choose>
								<xsl:when test="@Result='Passed'">

									<td style="color:green;font-weight:bold;">
										<xsl:value-of select="./@Result" />
									</td>

								</xsl:when>
								<xsl:when test="@Result='Warning'">

									<td style="color:orange;font-weight:bold;">
										<xsl:value-of select="./@Result" />
									</td>

								</xsl:when>
								<xsl:otherwise>

									<td style="color:red;font-weight:bold;">
										<xsl:value-of select="./@Result" />
									</td>

								</xsl:otherwise>
							</xsl:choose>

							<td>
								<a href="{@FolderHyperlink}">
									<xsl:value-of select="./@FolderData" />
								</a>
							</td>
							<td>
								<xsl:value-of select="Duration/@value" />
							</td>
						</tr>
					</xsl:for-each>
				</table>
				<br />
				<div id="header">DETAIL TEST RESULT</div>
				<br style="line-height: 5px;" />
				<table id="details" border="1" cellpadding="2"
					style="border-collapse: collapse;table-layout:fixed;">
					<tr bgcolor="#00b050" style="color:white;">
						<th style="text-align:left">
						</th>
						<th style="text-align:left">Step / Action Name</th>
						<th style="text-align:left">Status</th>
						<th style="text-align:left">SnapShot</th>
						<th style="text-align:left">Error Messages / Comments</th>
						<th style="text-align:left">Duration</th>
					</tr>
					<xsl:for-each select="TestResult/TestScenario">
						<tr bgcolor="#215967" style="color:white;font-weight:bold;">
							<td colspan="4">
								<a name="{position()}"></a>
								<xsl:value-of select="./@Title" />
							</td>
							<td style="text-align:right;border-left: 1px solid #215967">
								<a href="#" style="color:white;">Scroll To Top</a>
							</td>
							<td></td>
						</tr>
						<xsl:for-each select="Keyword">
							<tr style="border:0px;">
								<td style="width:20px;border:1px;"></td>
								<td colspan="4" bgcolor="#d8e4bc"
									style="color:#215967;font-weight:bold;font-size:12px;">
									<xsl:value-of select="./@Description" />
								</td>
<!-- 								<xsl:choose> -->
<!-- 									<xsl:when test="./@Description='Passed'"> -->
<!-- 										<td colspan="4" bgcolor="#d8e4bc" -->
<!-- 											style="color:#215967;font-weight:bold;font-size:12px;"> -->
<!-- 											<xsl:value-of select="./@Description" /> -->
<!-- 										</td> -->
<!-- 									</xsl:when> -->
<!-- 									<xsl:when test="./@Description='Warning'"> -->
<!-- 										<td colspan="4" bgcolor="#d8e4bc" -->
<!-- 											style="color:orange;font-weight:bold;font-size:12px;"> -->
<!-- 											<xsl:value-of select="./@Description" /> -->
<!-- 										</td> -->
<!-- 									</xsl:when> -->
<!-- 									<xsl:otherwise> -->
<!-- 										<td colspan="4" bgcolor="#d8e4bc" -->
<!-- 											style="color:red;font-weight:bold;font-size:12px;"> -->
<!-- 											<xsl:value-of select="./@Description" /> -->
<!-- 										</td> -->
<!-- 									</xsl:otherwise> -->
<!-- 								</xsl:choose> -->
								<td colspan="4" bgcolor="#d8e4bc"
									style="text-align:center;color:#215967;font-weight:bold;font-size:12px;">
									<xsl:value-of select="Duration/@value" />
								</td>
							</tr>
							<xsl:for-each select="Step">
								<tr>
									<td
										style="border-top: 1px solid white;border-bottom: 1px solid white;"></td>
									<td style="padding-left: 10px;">
										<xsl:value-of select="./@Description" />
									</td>
									<xsl:choose>
										<xsl:when test=".='Passed'">
											<td style="text-align:center;color:green;">
												<xsl:value-of select="." />
											</td>
										</xsl:when>
										<xsl:when test=".='Warning'">
											<td style="text-align:center;color:orange;">
												<xsl:value-of select="." />
											</td>
										</xsl:when>
										<xsl:otherwise>
											<td style="text-align:center;color:red;">
												<xsl:value-of select="." />
											</td>
										</xsl:otherwise>
									</xsl:choose>
									<td>
										<a href="{@SnapShotHyperlink}">
											<xsl:value-of select="./@SnapShotName" />
										</a>
									</td>
									<xsl:choose>
										<xsl:when test=".='Passed'">
											<td style="text-align:center;color:green;">
												<xsl:value-of select="./@ErrMsg" />
											</td>
										</xsl:when>
										<xsl:when test=".='Warning'">
											<td style="text-align:center;color:orange;">
												<xsl:value-of select="./@ErrMsg" />
											</td>
										</xsl:when>
										<xsl:otherwise>
											<td style="text-align:center;color:red;">
												<xsl:value-of select="./@ErrMsg" />
											</td>
										</xsl:otherwise>
									</xsl:choose>
									<td align="center">
										<xsl:value-of select="./@duration" />
									</td>
								</tr>
							</xsl:for-each>
						</xsl:for-each>
					</xsl:for-each>
				</table>
			</body>
		</html>
	</xsl:template>
</xsl:stylesheet>
