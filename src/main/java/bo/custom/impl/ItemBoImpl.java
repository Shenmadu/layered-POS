package bo.custom.impl;

import bo.custom.ItemBo;
import dao.DaoFactory;
import dao.custom.ItemDao;
import dao.util.DaoType;
import dto.CustomerDto;
import dto.ItemDto;
import entity.Customer;
import entity.Item;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ItemBoImpl implements ItemBo {
    private ItemDao itemDao= DaoFactory.getInstance().getDao(DaoType.ITEM);
    @Override
    public boolean saveItem(ItemDto itemDto) throws SQLException, ClassNotFoundException {
        return itemDao.save(new Item(
                itemDto.getCode(),
                itemDto.getDescription(),
                itemDto.getUnitPrice(),
                itemDto.getQtyOnHand()

        ));
    }

    @Override
    public boolean updateItem(ItemDto itemDto) throws SQLException, ClassNotFoundException {
        return itemDao.update(new Item(
                itemDto.getCode(),
                itemDto.getDescription(),
                itemDto.getUnitPrice(),
                itemDto.getQtyOnHand()
        ));
    }

    @Override
    public boolean deleteItem(String code) throws SQLException, ClassNotFoundException {
        return itemDao.delete(code);
    }

    @Override
    public List<ItemDto> allItems() throws SQLException, ClassNotFoundException {
        List<Item> entityList=itemDao.getAll();
        List<ItemDto> list=new ArrayList<>();
        for (Item item:entityList) {
            list.add(new ItemDto(
                    item.getCode(),
                    item.getDescription(),
                    item.getUnitPrice(),
                    item.getQtyOnHand()
            ));
        }
        return list;
    }

    @Override
    public ItemDto getItem(String code) throws SQLException, ClassNotFoundException {
        return itemDao.getItem(code);
    }


}
