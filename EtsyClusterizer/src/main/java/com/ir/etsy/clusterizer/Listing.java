package com.ir.etsy.clusterizer;

import java.util.List;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 *
 * @author Dimitar
 */
public class Listing {

    @JsonProperty("listing_id")
    private long listingId;

    @JsonProperty("state")
    private ListingState state;

    @JsonProperty("user_id")
    private long userId;

    @JsonProperty("title")
    private String title;

    @JsonProperty("description")
    private String description;

    @JsonProperty("creation_tsz")
    private double creationTsz;

    @JsonProperty("ending_tsz")
    private double endingTsz;

    @JsonProperty("tags")
    private List<String> tags;

    @JsonProperty("category_path")
    private List<String> categoryPath;

    @JsonProperty("category_path_ids")
    private List<Long> categoryPathIds;

    @JsonProperty("materials")
    private List<String> materials;

    @JsonProperty("views")
    private int views;

    @JsonProperty("num_favorers")
    private int numFavorers;

    @JsonProperty("is_supply")
    private boolean isSupply;

    @JsonProperty("occasion")
    private String occasion;

    @JsonProperty("style")
    private String style;

    @JsonProperty("has_variations")
    private boolean hasVariations;

    @JsonProperty("suggested_taxonomy_id")
    private long suggestedTaxonomyId;

    @JsonProperty("taxonomy_path")
    private List<String> taxonomyPath;

    @JsonProperty("used_manufacturer")
    private boolean usedManufacturer;

    public Listing() {
    }

    public long getListingId() {
        return listingId;
    }

    public void setListingId(long listingId) {
        this.listingId = listingId;
    }

    public ListingState getState() {
        return state;
    }

    public void setState(ListingState state) {
        this.state = state;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getCreation_tsz() {
        return creationTsz;
    }

    public void setCreation_tsz(double creation_tsz) {
        this.creationTsz = creation_tsz;
    }

    public double getEnding_tsz() {
        return endingTsz;
    }

    public void setEnding_tsz(double ending_tsz) {
        this.endingTsz = ending_tsz;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public List<String> getCategory_path() {
        return categoryPath;
    }

    public void setCategory_path(List<String> category_path) {
        this.categoryPath = category_path;
    }

    public List<Long> getCategory_path_ids() {
        return categoryPathIds;
    }

    public void setCategory_path_ids(List<Long> category_path_ids) {
        this.categoryPathIds = category_path_ids;
    }

    public List<String> getMaterials() {
        return materials;
    }

    public void setMaterials(List<String> materials) {
        this.materials = materials;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public int getNum_favorers() {
        return numFavorers;
    }

    public void setNum_favorers(int num_favorers) {
        this.numFavorers = num_favorers;
    }

    public boolean isIs_supply() {
        return isSupply;
    }

    public void setIs_supply(boolean is_supply) {
        this.isSupply = is_supply;
    }

    public String getOccasion() {
        return occasion;
    }

    public void setOccasion(String occasion) {
        this.occasion = occasion;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public boolean isHas_variations() {
        return hasVariations;
    }

    public void setHas_variations(boolean has_variations) {
        this.hasVariations = has_variations;
    }

    public long getSuggested_taxonomy_id() {
        return suggestedTaxonomyId;
    }

    public void setSuggested_taxonomy_id(long suggested_taxonomy_id) {
        this.suggestedTaxonomyId = suggested_taxonomy_id;
    }

    public List<String> getTaxonomy_path() {
        return taxonomyPath;
    }

    public void setTaxonomy_path(List<String> taxonomy_path) {
        this.taxonomyPath = taxonomy_path;
    }

    public boolean isUsed_manufacturer() {
        return usedManufacturer;
    }

    public void setUsed_manufacturer(boolean used_manufacturer) {
        this.usedManufacturer = used_manufacturer;
    }

    @Override
    public String toString() {
        return "Listing{"
                + "listing_id=" + listingId
                + ", state=" + state
                + ", user_id=" + userId
                + ", title=" + title
                + ", description=" + description
                + ", creation_tsz=" + creationTsz
                + ", ending_tsz=" + endingTsz
                + ", tags=" + tags
                + ", category_path=" + categoryPath
                + ", category_path_ids=" + categoryPathIds
                + ", materials=" + materials
                + ", views=" + views
                + ", num_favorers=" + numFavorers
                + ", is_supply=" + isSupply
                + ", occasion=" + occasion
                + ", style=" + style
                + ", has_variations=" + hasVariations
                + ", suggested_taxonomy_id=" + suggestedTaxonomyId
                + ", taxonomy_path=" + taxonomyPath
                + ", used_manufacturer=" + usedManufacturer + '}';
    }

}
