package com.ir.etsy.clusterizer.listings;

import com.ir.etsy.clusterizer.utils.ListingProperties;
import java.util.List;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 *
 * @author Dimitar
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Listing {

    @JsonProperty(ListingProperties.LISTING_ID)
    private long listingId;

    @JsonProperty(ListingProperties.STATE)
    private ListingState state;

    @JsonProperty(ListingProperties.USER_ID)
    private long userId;

    @JsonProperty(ListingProperties.TITLE)
    private String title;

    @JsonProperty(ListingProperties.CREATION_TSZ)
    private double creationTsz;

    @JsonProperty(ListingProperties.ENDING_TSZ)
    private double endingTsz;

    @JsonProperty(ListingProperties.TAGS)
    private List<String> tags;

    @JsonProperty(ListingProperties.CATEGORY_PATH)
    private List<String> categoryPath;

    @JsonProperty(ListingProperties.CATEGORY_PATH_IDS)
    private List<Long> categoryPathIds;

    @JsonProperty(ListingProperties.MATERIALS)
    private List<String> materials;

    @JsonProperty(ListingProperties.VIEWS)
    private int views;

    @JsonProperty(ListingProperties.NUM_FAVORERS)
    private int numFavorers;

    @JsonProperty(ListingProperties.IS_SUPPLY)
    private boolean isSupply;

    @JsonProperty(ListingProperties.OCCASION)
    private String occasion;

    @JsonProperty(ListingProperties.STYLE)
    private String style;

    @JsonProperty(ListingProperties.HAS_VARIATION)
    private boolean hasVariations;

    @JsonProperty(ListingProperties.SUGGESTED_TAXONOMY_ID)
    private long suggestedTaxonomyId;

    @JsonProperty(ListingProperties.TAXONOMY_PATH)
    private List<String> taxonomyPath;

    @JsonProperty(ListingProperties.USED_MANUFACTURER)
    private boolean usedManufacturer;

    public Listing() {
    }

    public Document toDocument() {
        Document document = new Document();
        document.add(new TextField(ListingProperties.TITLE, title, Field.Store.YES));
        document.add(new TextField(ListingProperties.TAGS, listToString(tags), Field.Store.YES));
        return document;
    }

    private static String listToString(List<String> list) {
        StringBuilder elementsBuilder = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            elementsBuilder.append(list.get(i));

            if (i < list.size() - 1) {
                elementsBuilder.append(" ");
            }
        }
        return elementsBuilder.toString();
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

    public double getCreationTsz() {
        return creationTsz;
    }

    public void setCreationTsz(double creationTsz) {
        this.creationTsz = creationTsz;
    }

    public double getEndingTsz() {
        return endingTsz;
    }

    public void setEndingTsz(double endingTsz) {
        this.endingTsz = endingTsz;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public List<String> getCategoryPath() {
        return categoryPath;
    }

    public void setCategoryPath(List<String> categoryPath) {
        this.categoryPath = categoryPath;
    }

    public List<Long> getCategoryPathIds() {
        return categoryPathIds;
    }

    public void setCategoryPathIds(List<Long> categoryPathIds) {
        this.categoryPathIds = categoryPathIds;
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

    public int getNumFavorers() {
        return numFavorers;
    }

    public void setNumFavorers(int numFavorers) {
        this.numFavorers = numFavorers;
    }

    public boolean isIsSupply() {
        return isSupply;
    }

    public void setIsSupply(boolean isSupply) {
        this.isSupply = isSupply;
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

    public boolean isHasVariations() {
        return hasVariations;
    }

    public void setHasVariations(boolean hasVariations) {
        this.hasVariations = hasVariations;
    }

    public long getSuggestedTaxonomyId() {
        return suggestedTaxonomyId;
    }

    public void setSuggestedTaxonomyId(long suggestedTaxonomyId) {
        this.suggestedTaxonomyId = suggestedTaxonomyId;
    }

    public List<String> getTaxonomyPath() {
        return taxonomyPath;
    }

    public void setTaxonomyPath(List<String> taxonomyPath) {
        this.taxonomyPath = taxonomyPath;
    }

    public boolean isUsedManufacturer() {
        return usedManufacturer;
    }

    public void setUsedManufacturer(boolean usedManufacturer) {
        this.usedManufacturer = usedManufacturer;
    }

    @Override
    public String toString() {
        return "Listing{"
                + "listing_id=" + listingId
                + ", state=" + state
                + ", user_id=" + userId
                + ", title=" + title
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
