package com.ir.etsy.clusterizer.listings;

import com.ir.etsy.clusterizer.utils.ListingProperties;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.LongPoint;
import org.apache.lucene.document.StringField;
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
    protected long listingId;

    @JsonProperty(ListingProperties.STATE)
    protected ListingState state;

    @JsonProperty(ListingProperties.USER_ID)
    protected long userId;

    @JsonProperty(ListingProperties.TITLE)
    protected String title;

    @JsonProperty(ListingProperties.CREATION_TSZ)
    protected long creationTsz;

    @JsonProperty(ListingProperties.ENDING_TSZ)
    protected long endingTsz;

    @JsonProperty(ListingProperties.TAGS)
    protected List<String> tags;

    @JsonProperty(ListingProperties.CATEGORY_PATH)
    protected List<String> categoryPath;

    @JsonProperty(ListingProperties.CATEGORY_PATH_IDS)
    protected List<Long> categoryPathIds;

    @JsonProperty(ListingProperties.MATERIALS)
    protected List<String> materials;

    @JsonProperty(ListingProperties.VIEWS)
    protected int views;

    @JsonProperty(ListingProperties.NUM_FAVORERS)
    protected int numFavorers;

    @JsonProperty(ListingProperties.IS_SUPPLY)
    protected boolean isSupply;

    @JsonProperty(ListingProperties.OCCASION)
    protected String occasion;

    @JsonProperty(ListingProperties.STYLE)
    protected List<String> style;

    @JsonProperty(ListingProperties.HAS_VARIATION)
    protected boolean hasVariations;

    @JsonProperty(ListingProperties.SUGGESTED_TAXONOMY_ID)
    protected long suggestedTaxonomyId;

    @JsonProperty(ListingProperties.TAXONOMY_PATH)
    protected List<String> taxonomyPath;

    @JsonProperty(ListingProperties.USED_MANUFACTURER)
    protected boolean usedManufacturer;

    public Listing() {
    }

    /**
     * Creates a tiny listing from a Document
     *
     * @param doc
     */
    public Listing(Document doc) {
        this.listingId = Integer.valueOf(doc.get(ListingProperties.LISTING_ID));
        this.title = doc.get(ListingProperties.TITLE);
        this.tags = Arrays.asList(doc.getValues(ListingProperties.TAGS));
        this.categoryPathIds = pathAsList(doc);
    }

    protected List<Long> pathAsList(Document doc) {
        int categoryIndex = 0;

        String p = doc.get(ListingProperties.CATEGORY + (categoryIndex++));
        if (p == null) {
            return Collections.EMPTY_LIST;
        }

        List<Long> path = new ArrayList<>();
        while (p != null) {
            path.add(Long.valueOf(p));
            p = doc.get(ListingProperties.CATEGORY + (categoryIndex++));
        }

        return path;
    }

    public Document toDocument() {
        Document doc = new Document();

        String listingIdStr = String.valueOf(listingId);
        doc.add(new StringField(ListingProperties.LISTING_ID, listingIdStr, Field.Store.YES));

        doc.add(new TextField(ListingProperties.TITLE, (title != null ? title : ""), Field.Store.YES));

        addListItems(doc, tags, ListingProperties.TAGS);

        int categoryIndex = 0;
        // There are only up to 3 categories in a hierarchy
        for (String category : categoryPath) {
            doc.add(new TextField(ListingProperties.CATEGORY + (categoryIndex++), category, Field.Store.YES));
        }

        addListItems(doc, materials, ListingProperties.MATERIALS);

        doc.add(new LongPoint(ListingProperties.CREATION_TSZ, creationTsz));
        doc.add(new LongPoint(ListingProperties.ENDING_TSZ, endingTsz));

        doc.add(new LongPoint(ListingProperties.VIEWS, views));
        doc.add(new LongPoint(ListingProperties.NUM_FAVORERS, numFavorers));

        return doc;
    }

    protected static void addListItems(Document doc, List<String> list, String fieldName) {
        for (String item : list) {
            doc.add(new TextField(fieldName, item, Field.Store.YES));
        }
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

    public void setCreationTsz(long creationTsz) {
        this.creationTsz = creationTsz;
    }

    public double getEndingTsz() {
        return endingTsz;
    }

    public void setEndingTsz(long endingTsz) {
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

    public List<String> getStyle() {
        return style;
    }

    public void setStyle(List<String> style) {
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
