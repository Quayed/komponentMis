package DAL;

import DTO.ComponentDTO;

/**
 * Created by mathias on 21/03/16.
 */
public interface IComponentDAO {
    int createComponent(ComponentDTO component);

    ComponentDTO getComponent(String barcode);
        
    ComponentDTO[] getComponents();

    int updateComponent(ComponentDTO component);

    int deleteComponent(String barcode);
}
