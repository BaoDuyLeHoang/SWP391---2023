package com.example.projectswp.repositories;

import com.example.projectswp.data_view_model.Item.ItemDeleteVM;
import com.example.projectswp.data_view_model.Item.DynamicFilterVM;
import com.example.projectswp.model.Items;
import com.example.projectswp.repositories.rowMapper.ItemsRowMapper;
import com.example.projectswp.repositories.ultil.Ultil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Repository
public class ItemsRepository {
    private static final ItemsRowMapper ITEMS_ROW_MAPPER = new ItemsRowMapper();
    @Autowired
    JdbcTemplate jdbcTemplate;
    public Date getCurrentDate(){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        LocalDateTime now = LocalDateTime.now();
        return new Date(dtf.format(now));
    }
    public Items getItem(int itemID) {
        String sql = "select * from Items where ItemID = ?";
        List<Items> items = jdbcTemplate.query(sql,ITEMS_ROW_MAPPER, itemID);
        return items.size() != 0? items.get(0): null;
    }
    public Items getItemDetail(int itemID) {
        String sql = "select * from Items where ItemID = ?";
        List<Items> items = jdbcTemplate.query(sql,ITEMS_ROW_MAPPER, itemID);
        return items.size() != 0? items.get(0): null;
    }
//    public Items getRequestDetail(int requestID) {
//        String sql = "select * from Items where ItemID = ?";
//        List<Items> items = jdbcTemplate.query(sql,ITEMS_ROW_MAPPER, itemID);
//        return items.size() != 0? items.get(0): null;
//    }
    public List<Items> getItems() {
        String sql = "Select * from Items";
        List<Items> items = jdbcTemplate.query(sql,ITEMS_ROW_MAPPER);
        return items.size() != 0? items: null;
    }
    public List<Items> getAllBriefItemAndBriefRequest(boolean share , boolean status, int pageNumber, int pageSize) {
        int itemsToSkip = 0;
        if(pageNumber !=0){
            itemsToSkip = (pageNumber - 1) * pageSize;
        }
        String sql = "SELECT * FROM Items WHERE Share = ? and Item_Status = ? ORDER BY ItemID OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
        List<Items> items = jdbcTemplate.query(sql,ITEMS_ROW_MAPPER, share, status, itemsToSkip, pageSize);
        return items.size() != 0? items: null;
    }
    public List<Items> getBriefItemByOrBriefRequestUserID(int userID, boolean status, boolean share, int pageNumber, int pageSize) {
        int itemsToSkip = 0;
        if(pageNumber !=0){
            itemsToSkip = (pageNumber - 1) * pageSize;
        }
        String sql = "Select * from Items where UserID =? and Item_Status=? and Share=? ORDER BY ItemID OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
        List<Items> items = jdbcTemplate.query(sql,ITEMS_ROW_MAPPER, userID, status, share, itemsToSkip, pageSize);
        return items.size() != 0? items: null;
    }
    public List<Items> searchBriefItemByItemTitle(String itemTitle, boolean status) {
        String sql = "Select * from Items where Item_Title like ? and Item_Status = ?";
        List<Items> items = jdbcTemplate.query(sql,ITEMS_ROW_MAPPER, "%" +itemTitle + "%", status);
        return items.size() != 0? items: null;
    }
    public List<Items> searchBriefItemBySubCategoryID(int subcategoryID, boolean status) {
        String sql = "Select * from Items where Sub_CategoryID = ? and Item_Status = ?";
        List<Items> items = jdbcTemplate.query(sql,ITEMS_ROW_MAPPER, subcategoryID, status);
        return items.size() != 0? items: null;
    }
    public List<Items> searchBriefItemByCategoryID(int categoryID, boolean status, boolean share, int pageNumber, int pageSize) {
        int itemsToSkip = 0;
        if(pageNumber !=0){
            itemsToSkip = (pageNumber - 1) * pageSize;
        }
        String sql = "select *\n" +
                "from dbo.Items item inner join dbo.SubCategories subcategory on item.Sub_CategoryID = subcategory.Sub_CategoryID\n" +
                "where subcategory.CategoryID = ? and Item_Status =? and Share=? ORDER BY ItemID OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
        List<Items> items = jdbcTemplate.query(sql,ITEMS_ROW_MAPPER, categoryID, status, share, itemsToSkip, pageSize);
        return items.size() != 0? items: null;
    }

    public Items getLastItem() {
        List<Items> list = getItems();
        return list.size() != 0 ? list.get(list.size()-1) : null;
    }
    public boolean addItems(Items item) {
        String sql = "insert into dbo.Items ([Item_Code], [UserID], [Sub_CategoryID], " +
                "[Item_Title], [Item_Detailed_Description], [Item_Mass], " +
                "[Item_Size], [Item_Quanlity], [Item_Estimate_Value], [Item_Sale_Price], " +
                "[Item_Share_Amount], [Item_Sponsored_Order_Shipping_Fee], [Item_Expired_Time], " +
                "[Item_Shipping_Address], [Item_Date_Created], [Item_Date_Update]," +
                "[Item_Status],[Share], [Image])\n" +
                "values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        int check = jdbcTemplate.update(sql, Ultil.getUUID(), Ultil.getUserId(), item.getSubCategoryId(), item.getItemTitle(),
                item.getItemDetailedDescription(), item.getItemMass(), item.isItemSize(),  item.getItemQuanlity(),
                item.getItemEstimateValue(), item.getItemSalePrice(), item.getItemShareAmount(), item.isItemSponsoredOrderShippingFee(),
                item.getStringDateTimeExpired(), item.getItemShippingAddress(), getCurrentDate(), null,item.isStatus(),item.isShare(),
                item.getImage());
        return check != 0;
    }

    public boolean updateItems(Items item) {
        String sql = "update dbo.Items\n" +
                "set \n" +
                "    Sub_CategoryID = ?,\n" +
                "    Item_Title = ?,\n" +
                "    Item_Detailed_Description = ?,\n" +
                "    Item_Mass = ?,\n" +
                "    Item_Size = ?,\n" +
                "    Item_Quanlity = ?,\n" +
                "    Item_Estimate_Value = ?,\n" +
                "    Item_Sale_Price = ?,\n" +
                "    Item_Share_Amount = ?,\n" +
                "    Item_Sponsored_Order_Shipping_Fee = ?,\n" +
                "    Item_Shipping_Address = ?,\n" +
                "    Image = ?,\n" +
                "    Item_Expired_Time = ?,\n" +
                "    Share = ?,\n" +
                "    Item_Date_Update = ?,\n" +
                "where ItemID = ? and UserID =?";

        int check = jdbcTemplate.update(sql,item.getUserId(),
                item.getSubCategoryId(), item.getItemTitle(),
                item.getItemDetailedDescription(), item.getItemMass(),
                item.isItemSize(),  item.getItemQuanlity(),
                item.getItemEstimateValue(), item.getItemSalePrice(),
                item.getItemShareAmount(), item.isItemSponsoredOrderShippingFee(),
                item.getItemShippingAddress(),item.getImage(),
                item.getStringDateTimeExpired(), item.isShare(),
                getCurrentDate(), item.getItemID(),Ultil.getUserId());
        return check != 0;
    }
    public boolean deleteItem(ItemDeleteVM itemDeleteVM){
        String sql = "update dbo.Items set Item_Status = ? where ItemID=?";
        int check = jdbcTemplate.update(sql, false, itemDeleteVM.getItemID());
        return check > 0;
    }
    public List<Items> getAllBriefItemAndBriefRequestByUserID(int userID, boolean share) {
        String sql = "Select * from Items where UserID = ? and Share = ?";
        List<Items> items = jdbcTemplate.query(sql,ITEMS_ROW_MAPPER, userID ,share);
        return items.size() != 0? items: null;
    }
    public List<Items> getAllShareRecently(int pageNumber, int pageSize, int userID) {
        int itemsToSkip = 0;
        if(pageNumber !=0){
            itemsToSkip = (pageNumber - 1) * pageSize;
        }
        String sql = "Select * from Items where UserID = ? ORDER BY ItemID OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
        List<Items> items = jdbcTemplate.query(sql,ITEMS_ROW_MAPPER, userID ,itemsToSkip, pageSize);
        return items.size() != 0? items: null;
    }
    public List<Items> getAllShareFree(int pageNumber, int pageSize, int userID) {
        int itemsToSkip = 0;
        if(pageNumber !=0){
            itemsToSkip = (pageNumber - 1) * pageSize;
        }
        String sql = "Select * from Items where UserID = ? and Item_Sale_Price = 0 ORDER BY ItemID OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
        List<Items> items = jdbcTemplate.query(sql,ITEMS_ROW_MAPPER, userID ,itemsToSkip, pageSize);
        return items.size() != 0? items: null;
    }
    public List<Items> getAllMyShareAndRequest(boolean share, boolean status, int pageNumber, int pageSize) {
        int itemsToSkip = 0;
        if(pageNumber !=0){
            itemsToSkip = (pageNumber - 1) * pageSize;
        }
        String sql = "Select * from Items where UserID = ? and Share = ? and Item_Status = ? ORDER BY ItemID OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
        List<Items> items = jdbcTemplate.query(sql,ITEMS_ROW_MAPPER, Ultil.getUserId() ,share , status,itemsToSkip, pageSize);
        return items.size() != 0? items: null;
    }
    public List<Items> getListAllOtherPersonRequestItem(boolean share, boolean status, int pageNumber, int pageSize) {
        int itemsToSkip = 0;
        if(pageNumber !=0){
            itemsToSkip = (pageNumber - 1) * pageSize;
        }
        String sql = "Select * from Items where UserID = ? and Share = ? and Item_Status = ? ORDER BY ItemID OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
        List<Items> items = jdbcTemplate.query(sql,ITEMS_ROW_MAPPER, Ultil.getUserId() ,share , status, itemsToSkip, pageSize);
        return items.size() != 0? items: null;
    }
    public List<Items> getListAllMyRequestItem(boolean share, boolean status, int pageNumber, int pageSize) {
        int itemsToSkip = 0;
        if(pageNumber !=0){
            itemsToSkip = (pageNumber - 1) * pageSize;
        }
        String sql = "Select * from Items where UserID = ? and Share = ? and Item_Status = ? ORDER BY ItemID OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
        List<Items> items = jdbcTemplate.query(sql,ITEMS_ROW_MAPPER, Ultil.getUserId() ,share , status, itemsToSkip, pageSize);
        return items.size() != 0? items: null;
    }
    public List<Items> getItemDynamicFilters(int pageNumber, int pageSize, DynamicFilterVM dynamicFilterVM) {
        int itemsToSkip = 0;
        if(pageNumber !=0){
            itemsToSkip = (pageNumber - 1) * pageSize;
        }
        String sql =  "Select * from Items";
        int count= 0;
        if(dynamicFilterVM.getCategoryID() !=0 && count == 0){
            sql = sql + " item inner join dbo.SubCategories subcategory on item.Sub_CategoryID = subcategory.Sub_CategoryID where subcategory.CategoryID = " + String.valueOf(dynamicFilterVM.getCategoryID());
            count = 1;
        }
        if(dynamicFilterVM.getTitleName() != null && count == 0) {
            sql = sql + " Where Item_Title = "  + "'" + String.valueOf(dynamicFilterVM.getTitleName() +"%") +"'";
            count = 1;
        }
        if(dynamicFilterVM.getTitleName() != null && count == 1){
            sql = sql + " And Item_Title like "  + "'" + String.valueOf(dynamicFilterVM.getTitleName() +"%") +"'";
        }
        //////////////////////////////////////
        if(dynamicFilterVM.getMaxPrice() >= dynamicFilterVM.getMinPrice() && count == 0) {
            sql = sql + " Where Item_Sale_Price <= " +String.valueOf(dynamicFilterVM.getMaxPrice()) +" and Item_Sale_Price >="
                    + String.valueOf(dynamicFilterVM.getMaxPrice());
            count = 1;
        }
        else{
            sql = sql + " And Item_Sale_Price <= " +String.valueOf(dynamicFilterVM.getMaxPrice()) +"and Item_Sale_Price >="
                    + String.valueOf(dynamicFilterVM.getMaxPrice());
        }
        sql = sql + " and Item_Estimate_Value <= " +String.valueOf(dynamicFilterVM.getMaxUsable()) +" and Item_Estimate_Value >="
                + String.valueOf(dynamicFilterVM.getMinUsable());

        sql = sql + " ORDER BY ItemID OFFSET " + String.valueOf(itemsToSkip)+ " ROWS FETCH NEXT " + String.valueOf(pageSize)+ " ROWS ONLY";

        List<Items> items = jdbcTemplate.query(sql,ITEMS_ROW_MAPPER);
        return items.size() != 0? items: null;
    }



}
