package cn.qtone.eims.bbgl.fymxb.util;

import java.util.HashMap;
import java.util.Map;

import jxl.Cell;
import jxl.Range;
import jxl.Sheet;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.CellFormat;
import jxl.format.Colour;
import jxl.format.VerticalAlignment;
import jxl.write.WritableWorkbook;

public class JXLReadExcel2 {
 

 
 public static String getExcelInfo(WritableWorkbook rwb) throws Exception{
	 
  StringBuffer sb = new StringBuffer();
//  
//  // File sourcefile = new File(path);
//  //InputStream is = new FileInputStream(sourcefile); 
//  
//  
//  ByteArrayOutputStream baos = new ByteArrayOutputStream();  
//  xwb.write(baos);
//  InputStream is = new ByteArrayInputStream(baos.toByteArray());
//  
  
  
 // Workbook rwb = Workbook.getWorkbook(is); 
  
  Sheet sheet = rwb.getSheet(0);
  
  int colnum = sheet.getColumns();
  
  int rownum = sheet.getRows();
  
  Map<String,String> map[] = getRowSpanColSpanMap(sheet);
  
  sb.append("<table border='1' cellspacing='0' width='1250'>");
  
  for(int row = 0; row < rownum; row ++) {
   
   sb.append("<tr>");
   
   for(int col = 0; col < colnum; col ++) {
    
    Cell cell = sheet.getCell(col, row);
    
    String content = cell.getContents();
    
    CellFormat cellFormat = cell.getCellFormat();
    
    if(map[0].containsKey(row + "," + col)) {
     
     String pointString = map[0].get(row + "," + col);
     
     map[0].remove(row + "," + col);
     
     int bottomeRow = Integer.valueOf(pointString.split(",")[0]);
     
     int bottomeCol = Integer.valueOf(pointString.split(",")[1]);
     
     int rowSpan = bottomeRow - row + 1;
     
     int colSpan = bottomeCol - col + 1;
     
     sb.append("<td rowspan= '" + rowSpan + "' colspan= '" + colSpan + "' " );
     
    } else if(map[1].containsKey(row + "," + col)){
     
     map[1].remove(row + "," + col);
     
     continue;
    } else{
     
     sb.append("<td ");
    }
    
    if(cellFormat != null){
     
     Alignment alignment = cellFormat.getAlignment();
     
     sb.append("align='" + convertToHtmlGrammer(alignment) + "' ");
     
     VerticalAlignment verticalAlignment = cellFormat.getVerticalAlignment();
     
     sb.append("valign='" + convertToHtmlGrammer(verticalAlignment) +"' ");
     
     sb.append("style='color:" + convertToHtmlGrammer(cellFormat.getFont().getColour()) + ";");
     
     Colour bgcolour = cellFormat.getBackgroundColour();
     
     sb.append("background-color:" + convertToHtmlGrammer(bgcolour) + ";");
     
     Colour bottomColour = cellFormat.getBorderColour(Border.BOTTOM);
     
     Colour leftColour = cellFormat.getBorderColour(Border.LEFT);
     
     Colour rightColour = cellFormat.getBorderColour(Border.RIGHT);
     
     Colour topColour = cellFormat.getBorderColour(Border.TOP);
     
//     System.out.print("bottomColour:" + bottomColour.getDescription());
//     System.out.print(" leftColour:" + leftColour.getDescription());
//     System.out.print(" rightColour:" + rightColour.getDescription());
//     System.out.println(" topColour:" + topColour.getDescription());
//2013-1-6    // sb.append("border-color:" + convertToHtmlGrammer(bottomColour) + ";");
     
     sb.append("' ");
    }
    
    sb.append(">");
    
    if(content == null || "".equals(content.trim())){
     
     sb.append(" &nbsp; ");
    }else{
    
     sb.append(content);
    }
    
    sb.append("</td>");
    
   }
   
   sb.append("</tr>");
  }
  
  sb.append("</table>");
  
  //rwb.close();
  
  //is.close();
  
  return sb.toString();
 }
 
 @SuppressWarnings("unchecked")
 private static Map<String,String>[] getRowSpanColSpanMap(Sheet sheet){
  
  Map<String,String> map0 = new HashMap<String,String>();
  Map<String,String> map1 = new HashMap<String,String>();

  Range[] range = sheet.getMergedCells();
  
  for(int i = 0; i < range.length; i ++){
  
   Cell topCell = range[i].getTopLeft();
   
   Cell bottomCell = range[i].getBottomRight();
   
   int topRow = topCell.getRow();
   
   int topCol = topCell.getColumn();
   
   int bottomRow = bottomCell.getRow();
   
   int bottomCol = bottomCell.getColumn();
   
   map0.put(topRow + "," + topCol, bottomRow + "," + bottomCol);
   
   int tempRow = topRow;
   
   while(tempRow <= bottomRow ){

    int tempCol = topCol;
    
    while(tempCol <= bottomCol ){
     
     map1.put(tempRow + "," + tempCol,"");
     
     tempCol ++;
    }
    
    tempRow ++;
   }
   
   map1.remove(topRow + "," + topCol);
   
  }
  
  Map[] map = {map0,map1};
  
  return map;
 }
 
 private static String convertToHtmlGrammer(Alignment alignment){
  
  String align = "left";
  
  switch(alignment.getValue()){
   
   case 1:
    align = "left";
    break;
   case 2:
    align = "center";
    break;
   case 3:
    align = "right";
    break;
   case 5:
    align = "justify";
    break;
            
   default: break;
  }
  
  return align;
 }
 private static String convertToHtmlGrammer(VerticalAlignment verticalAlignment){
  
  String valign = "middle";
  
  switch(verticalAlignment.getValue()){
   
   case 1:
    valign = "middle";
    break;
   case 2:
    valign = "bottom";
    break;
   case 3:
    valign = "top";
    break;
   default: break;
  }
  
  return valign;
 }
 private static String convertToHtmlGrammer(Colour colour){
  
  StringBuffer sb = new StringBuffer("");
  
  if(colour != null && !"default background".equalsIgnoreCase(colour.getDescription())){
   
   sb.append("#");
   sb.append(fillWithZero(Integer.toHexString(colour.getDefaultRGB().getRed()))) ;
   sb.append(fillWithZero(Integer.toHexString(colour.getDefaultRGB().getGreen()))) ;
   sb.append(fillWithZero(Integer.toHexString(colour.getDefaultRGB().getBlue()))) ;
  }
  
  return sb.toString();
 }
 
 private static String fillWithZero(String str){
  
  if(str != null && str.length() < 2){
   
   return "0" + str;
  }
   return str;
 }
  
 

 
}

