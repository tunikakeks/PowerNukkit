package cn.nukkit.blockproperty.value;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ChemistryTableType {

    COMPOUND_CREATOR(0, "compound_creator", "Compound Creator"),
    
    MATERIAL_REDUCER(1, "material_reducer", "Material Reducer"),
    
    ELEMENT_CONSTRUCTOR(2, "element_constructor", "Element Constructor"),
    
    LAB_TABLE(3, "lab_table", "Lab Table");

    private final int bit;
    private final String value;
    private final String englishName;

    public int getBit() {
        return bit;
    }

    public String getValue() {
        return value;
    }

    public String getEnglishName() {
        return englishName;
    }
}
