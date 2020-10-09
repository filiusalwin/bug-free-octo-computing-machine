package nl.miwgroningen.se.ch3.bacchux.model;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

public class CategoryDTO {

    private int categoryId;
    private String name;

    public CategoryDTO(Category category) {
        categoryId = category.getCategoryId();
        name = category.getName();
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
