package com.bluetag.wc.api;

/**
 * Created by Jeffy on 4/1/2017.
 */

import com.bluetag.wc.model.GroupModel;
import com.bluetag.wc.model.PastWinnersModel;
import com.bluetag.wc.model.StadiumModel;
import com.bluetag.wc.model.TeamModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;


public interface ApiInterface {
    @GET("get_all_stadiums.php")
    Call<StadiumModel> getStadiumsList();

    @GET("get_past_winners.php")
    Call<PastWinnersModel> getPastWinnersList();

    @GET("get_groups.php")
    Call<GroupModel> getGroups();

    @GET("get_group_teams.php")
    Call<TeamModel> getGroupTeams(@Query("group_id") int groupId);
}