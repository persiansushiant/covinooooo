package ir.technopedia.covino.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Poke {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("poker_id")
    @Expose
    private String pokerId;
    @SerializedName("poked_id")
    @Expose
    private String pokedId;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("is_new")
    @Expose
    private String isNew;
    @SerializedName("poker_phone")
    @Expose
    private String pokerPhone;
    @SerializedName("message")
    @Expose
    private String message;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPokerId() {
        return pokerId;
    }

    public void setPokerId(String pokerId) {
        this.pokerId = pokerId;
    }

    public String getPokedId() {
        return pokedId;
    }

    public void setPokedId(String pokedId) {
        this.pokedId = pokedId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getIsNew() {
        return isNew;
    }

    public void setIsNew(String isNew) {
        this.isNew = isNew;
    }

    public String getPokerPhone() {
        return pokerPhone;
    }

    public void setPokerPhone(String pokerPhone) {
        this.pokerPhone = pokerPhone;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}