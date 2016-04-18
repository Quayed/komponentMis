package DAL;

/**
 * Created by mathias on 21/03/16.
 */
public interface IComponentDAO {
    int createComponent(ComponentDTO component);

    ComponentDTO getComponent(String param, String value);
    
    ComponentDTO[] getComponents();

    int updateComponent(ComponentDTO component);

    int deleteComponent(int componentId);
}
