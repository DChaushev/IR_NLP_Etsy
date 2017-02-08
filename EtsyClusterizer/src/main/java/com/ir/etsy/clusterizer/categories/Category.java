package com.ir.etsy.clusterizer.categories;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.IntPoint;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Category {

    @JsonProperty(CategoryProperties.CATEGORY_ID)
    protected Long categoryId;

    @JsonProperty(CategoryProperties.NAME)
    protected String name;

    @JsonProperty(CategoryProperties.META_TITLE)
    protected String metaTitle;

    @JsonProperty(CategoryProperties.META_KEYWORDS)
    protected String metaKeywords;

    @JsonProperty(CategoryProperties.PAGE_TITLE)
    protected String pageTitle;

    @JsonProperty(CategoryProperties.SHORT_NAME)
    protected String shortName;

    @JsonProperty(CategoryProperties.NUM_CHILDREN)
    protected Integer numChildren;

    @JsonProperty(CategoryProperties.LEVEL)
    protected Integer level;

    @JsonProperty(CategoryProperties.PARENT_ID)
    protected Long parentId;

    @JsonProperty(CategoryProperties.GRANDPARENT_ID)
    protected Long grandparentId;

    public Category() {
    }
    
    /**
     * Creates a slim Category from a Document
     *
     * @param doc
     */
    public Category(Document doc) {
        this.categoryId = Long.valueOf(doc.get(CategoryProperties.CATEGORY_ID));
        this.metaTitle = doc.get(CategoryProperties.META_TITLE);
        this.metaKeywords = doc.get(CategoryProperties.META_KEYWORDS);
        this.level = Integer.valueOf(doc.get(CategoryProperties.LEVEL));
        this.parentId = stringToLong(doc.get(CategoryProperties.PARENT_ID));
        this.grandparentId = stringToLong(doc.get(CategoryProperties.GRANDPARENT_ID));
    }
    
    private static Long stringToLong(String val) {
        if (val == null || val.compareTo("null") == 0) {
            return null;
        }
        return Long.valueOf(val);
    }

    public Document toDocument() {
        Document doc = new Document();

        doc.add(new StringField(CategoryProperties.CATEGORY_ID, String.valueOf(categoryId), Field.Store.YES));
        doc.add(new TextField(CategoryProperties.META_TITLE, (metaTitle != null ? metaTitle : ""), Field.Store.YES));
        doc.add(new TextField(CategoryProperties.META_KEYWORDS, (metaKeywords != null ? metaKeywords : ""), Field.Store.YES));
        doc.add(new StringField(CategoryProperties.LEVEL, String.valueOf(level), Field.Store.YES));
        doc.add(new StringField(CategoryProperties.PARENT_ID, String.valueOf(parentId), Field.Store.YES));
        doc.add(new StringField(CategoryProperties.GRANDPARENT_ID, String.valueOf(grandparentId), Field.Store.YES));

        return doc;
    }

    @Override
    public String toString() {
        return "Category{"
                + "category_id=" + categoryId
                + ", name=" + name
                + ", metaTitle=" + metaTitle
                + ", metaKeywords=" + metaKeywords
                + ", pageTitle=" + pageTitle
                + ", shortName=" + shortName
                + ", numChildren=" + numChildren
                + ", level=" + level
                + ", parentId=" + parentId
                + ", grandparentId=" + grandparentId + '}';
    }

    /**
     * @return the categoryId
     */
    public Long getCategoryId() {
        return categoryId;
    }

    /**
     * @param categoryId the categoryId to set
     */
    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the metaTitle
     */
    public String getMetaTitle() {
        return metaTitle;
    }

    /**
     * @param metaTitle the metaTitle to set
     */
    public void setMetaTitle(String metaTitle) {
        this.metaTitle = metaTitle;
    }

    /**
     * @return the metaKeywords
     */
    public String getMetaKeywords() {
        return metaKeywords;
    }

    /**
     * @param metaKeywords the metaKeywords to set
     */
    public void setMetaKeywords(String metaKeywords) {
        this.metaKeywords = metaKeywords;
    }

    /**
     * @return the pageTitle
     */
    public String getPageTitle() {
        return pageTitle;
    }

    /**
     * @param pageTitle the pageTitle to set
     */
    public void setPageTitle(String pageTitle) {
        this.pageTitle = pageTitle;
    }

    /**
     * @return the shortName
     */
    public String getShortName() {
        return shortName;
    }

    /**
     * @param shortName the shortName to set
     */
    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    /**
     * @return the numChildren
     */
    public Integer getNumChildren() {
        return numChildren;
    }

    /**
     * @param numChildren the numChildren to set
     */
    public void setNumChildren(Integer numChildren) {
        this.numChildren = numChildren;
    }

    /**
     * @return the level
     */
    public Integer getLevel() {
        return level;
    }

    /**
     * @param level the level to set
     */
    public void setLevel(Integer level) {
        this.level = level;
    }

    /**
     * @return the parentId
     */
    public Long getParentId() {
        return parentId;
    }

    /**
     * @param parentId the parentId to set
     */
    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    /**
     * @return the grandparentId
     */
    public Long getGrandparentId() {
        return grandparentId;
    }

    /**
     * @param grandparentId the grandparentId to set
     */
    public void setGrandparentId(Long grandparentId) {
        this.grandparentId = grandparentId;
    }

}
