package DAL;

/**
 * Created by mathias on 21/03/16.
 */
public interface IComponentGroupDAO {
    int createComponentGroup(ComponentGroupDTO componentGroup);
    ComponentGroupDTO getComponentGroup(int componentGroupId);
    ComponentGroupDTO[] getComponentGroups();
    int updateComponentGroups(ComponentGroupDTO componentGroup);
    int deleteComponentGroup(int componentGroupId);
}
