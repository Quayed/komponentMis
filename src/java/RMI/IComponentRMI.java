/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RMI;

/**
 *
 * @author hippomormor
 */
public interface IComponentRMI {
    
    public String getBarcode();
    
    public int getComponentId();
       
    public int getComponentGroupId();
    
    public int getComponentNumber();

    public int getStatus();    
}
