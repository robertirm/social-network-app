package socialNetwork.service;

import socialNetwork.domain.Entity;

import java.util.List;

public interface StatisticsService<ID, E extends Entity<ID>> {
    int getNumberOfCommunities();
    List<Integer> getTheMostSocialCommunity();
}
