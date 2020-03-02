package ir.technopedia.covino.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PokeResponse {

    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("pokes")
    @Expose
    private List<Poke> pokes = null;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public List<Poke> getPokes() {
        return pokes;
    }

    public void setPokes(List<Poke> pokes) {
        this.pokes = pokes;
    }

}