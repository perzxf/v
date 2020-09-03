<!DOCTYPE html>
<html lang="en">

<head>
    <meta http-equiv=Content-Type content="text/html; charset=utf-8"/>
    <meta name=Generator content="Microsoft Word 15 (filtered)"/>
    <style>
        <!--
         /* Font Definitions */
         @font-face
            {font-family:宋体;
            panose-1:2 1 6 0 3 1 1 1 1 1;}
        @font-face
            {font-family:"Cambria Math";
            panose-1:2 4 5 3 5 4 6 3 2 4;}
        @font-face
            {font-family:Calibri;
            panose-1:2 15 5 2 2 2 4 3 2 4;}
        @font-face
            {font-family:方正小标宋简体;
            panose-1:2 0 0 0 0 0 0 0 0 0;}
        @font-face
            {font-family:仿宋;
            panose-1:2 1 6 9 6 1 1 1 1 1;}
        @font-face
            {font-family:"\@宋体";
            panose-1:2 1 6 0 3 1 1 1 1 1;}
        @font-face
            {font-family:"\@方正小标宋简体";
            panose-1:2 0 0 0 0 0 0 0 0 0;}
        @font-face
            {font-family:"\@仿宋";
            panose-1:2 1 6 9 6 1 1 1 1 1;}
         /* Style Definitions */
         p.MsoNormal, li.MsoNormal, div.MsoNormal
            {margin:0cm;
            margin-bottom:.0001pt;
            text-align:center;
            text-justify:inter-ideograph;
            font-size:10.5pt;
            font-family:"Calibri","sans-serif";}
        .MsoChpDefault
            {font-family:"Calibri","sans-serif";}

        -->
    </style>

</head>

<body  style='text-justify-trim:punctuation'>

     <div class="" >

     <p class="MsoNormal" align=center style='margin-top:150pt;text-align:center'>
      <span style='font-size:24.0pt;
     font-family:方正小标宋简体;color:red'>安徽楼事网络科技有限公司</span></p>



     <hr style="size:20px; color:#FF0000; width:78%" />


     <p class="MsoNormal" align=center style='text-align:center'><span
     style='font-size:22.0pt;font-family:方正小标宋简体'>关于${monitorName}项目的舆情监测报告</span></p>

     <p class="MsoNormal" style='margin-top:15.6pt;text-indent:32.0pt'><span
      style='font-size:16.0pt;font-family:仿宋'>${monitorName}</span><span
     style='font-size:16.0pt;font-family:仿宋'>项目监测到数据共${totalCount}条，其中正面记录${goodCount}条，负面记录${badCount}条，中性记录${otherCount}条。</span></p>

     <p class="MsoNormal" style='margin-bottom:23.4pt;text-indent:32.0pt'><span
     style='font-size:16.0pt;font-family:仿宋'>下面为部分记录的标题和发生时间：</span></p>

     <table class="MsoTableGrid" border=1 cellspacing=0 cellpadding=0
      style='border-collapse:collapse;border:none' align="center">
      <tr>
           <td width=245 valign=top style='width:147.2pt;border:solid windowtext 1.0pt;
           padding:0cm 5.4pt 0cm 5.4pt'>
           <p class="MsoNormal" align=center style='text-align:center'><span
           style='font-size:16.0pt;font-family:仿宋'>标题</span></p>
           </td>
           <td width=245 valign=top style='width:147.25pt;border:solid windowtext 1.0pt;
           border-left:none;padding:0cm 5.4pt 0cm 5.4pt'>
           <p class="MsoNormal" align=center style='text-align:center'><span
           style='font-size:16.0pt;font-family:仿宋'>链接</span></p>
           </td>
           <td width=245 valign=top style='width:147.25pt;border:solid windowtext 1.0pt;
           border-left:none;padding:0cm 5.4pt 0cm 5.4pt'>
           <p class="MsoNormal" align=center style='text-align:center'><span
           style='font-size:16.0pt;font-family:仿宋'>时间</span></p>
       </td>
      </tr>
         <#list cellgrids as cellgrid>
      <tr>
       <td width=245 valign=top style='width:147.2pt;border:solid windowtext 1.0pt;
       border-top:none;padding:0cm 5.4pt 0cm 5.4pt'>
       <p class="MsoNormal" align="center"><span style='font-size:16.0pt;font-family:仿宋'>${cellgrid[0]}&nbsp;</span></p>
       </td>
       <td width=245 valign=top style='width:147.25pt;border-top:none;border-left:
       none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
       padding:0cm 5.4pt 0cm 5.4pt'>
       <p class="MsoNormal"><span style='font-size:16.0pt;font-family:仿宋'>${cellgrid[1]}&nbsp;</span></p>
       </td>
       <td width=245 valign=top style='width:147.25pt;border-top:none;border-left:
       none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
       padding:0cm 5.4pt 0cm 5.4pt'>
       <p class="MsoNormal" align="center"><span style='font-size:16.0pt;font-family:仿宋'>${cellgrid[2]}&nbsp;</span></p>
       </td>
      </tr>
         </#list>
     </table>

         <div style="text-align: right">${time}</div>

     </div>

</body>

</html>
