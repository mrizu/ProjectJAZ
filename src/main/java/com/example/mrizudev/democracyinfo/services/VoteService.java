package com.example.mrizudev.democracyinfo.services;

import com.example.mrizudev.democracyinfo.repositories.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.List;

@Component
public class VoteService {
    @PersistenceContext
    EntityManager entityManager;
    VoteRepository voteRepository;

    @Autowired
    public VoteService(VoteRepository voteRepository){
        this.voteRepository = voteRepository;
    }

    @Transactional
    public void addVote(String username, int option_id) {
        int user_id = this.getIdByName(username);
        String queryBody = "INSERT INTO vote (user_id, option_id) VALUES (:userId, :optionId)";
        Query query = entityManager.createNativeQuery(queryBody);
        query.setParameter("userId", user_id);
        query.setParameter("optionId", option_id);
        query.executeUpdate();
    }

    public int getPollIdByOptionId(int option_id) {
        String queryBody = "SELECT option.poll_id FROM option WHERE option.id = :optionId";
        Query query = entityManager.createNativeQuery(queryBody);
        query.setParameter("optionId", option_id);
        List res = query.getResultList();
        if (res.size() > 0) {
            int result = (int)res.get(0);
            return result;
        } else {
            return -1;
        }
    }

    private int getIdByName(String login) {
        String queryBody = "SELECT * FROM user WHERE username = :username";
        Query query = entityManager.createNativeQuery(queryBody);
        query.setParameter("username", login);
        List<Object[]> res = query.getResultList();
        if (res.size() > 0) {
            return (int) res.get(0)[UserService.USER_COLUMN_ID];
        } else {
            return -1;
        }
    }

    public String getPollResults() {
        String pollQuestion = this.getPollQuestionByPollId(1);
        List<Object[]> results = this.getNumbersOfVotesByPollId(1);
        String data = "{\"pollQuestion\":\""+pollQuestion+"\",\"results\":[";
        for (Object[] option : results) {
            data += "{\"option_name\":\"" + option[0] + "\",\"votes_number\":" + option[1] + "},";
        }
        data = data.substring(0, data.length()-1);
        data += "]}";
        return data;
    }

    private List<Object[]> getNumbersOfVotesByPollId(int poll_id) {
        String queryBody = "SELECT option.option_text AS 'partyName', COUNT(vote.option_id) as 'voteCount' FROM vote INNER JOIN option ON option.id = vote.option_id WHERE option.poll_id = :pollId GROUP BY option_id, option_text ";
        Query query = entityManager.createNativeQuery(queryBody);
        query.setParameter("pollId", poll_id);
        return query.getResultList();
    }

    private void getOptionsTextByPollId(int poll_id) {
        String queryBody = "SELECT option_text FROM user WHERE poll_id = :poll_id";
        Query query = entityManager.createNativeQuery(queryBody);
        query.setParameter("poll_id", poll_id);
        List<Object[]> res = query.getResultList();
    }

    private String getPollQuestionByPollId(int poll_id) {
        String queryBody = "SELECT question FROM poll WHERE poll.id = :pollId";
        Query query = entityManager.createNativeQuery(queryBody);
        query.setParameter("pollId", poll_id);
        String res = query.getResultList().get(0).toString();
        return res;
    }

}
