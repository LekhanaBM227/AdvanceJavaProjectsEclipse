package com.quizapp.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "quiz_attempts")
public class QuizAttempt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quiz_id", nullable = false)
    private Quiz quiz;

    @Column(name = "attempt_date")
    private LocalDateTime attemptDate;

    @Column(name = "completed_date")
    private LocalDateTime completedDate;

    @Column(name = "score")
    private Double score = 0.0;

    @Column(name = "is_passed")
    private Boolean isPassed;

    @Column(name = "total_score")
    private Integer totalScore = 0;

    @Column(name = "frozen_score")
    private Integer frozenScore = 0;

    @Column(name = "current_level")
    private Integer currentLevel = 1;

    @Column(name = "current_question_index")
    private Integer currentQuestionIndex = 0;

    @Column(name = "lifeline_fifty_fifty_used")
    private Boolean lifelineFiftyFiftyUsed = false;

    @Column(name = "lifeline_audience_poll_used")
    private Boolean lifelineAudiencePollUsed = false;

    @Column(name = "lifeline_skip_question_used")
    private Boolean lifelineSkipQuestionUsed = false;

    @Column(name = "is_completed")
    private Boolean isCompleted = false;

    @Column(name = "time_remaining_seconds")
    private Integer timeRemainingSeconds;

    @OneToMany(mappedBy = "quizAttempt", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<UserAnswer> userAnswers = new ArrayList<>();

    // Constructors
    public QuizAttempt() {
        this.attemptDate = LocalDateTime.now();
    }

    public QuizAttempt(User user, Quiz quiz) {
        this();
        this.user = user;
        this.quiz = quiz;
        if (quiz.getTimeLimit() != null) {
            this.timeRemainingSeconds = quiz.getTimeLimit();
        }
    }

    // Getters and Setters
    public Long getId() { 
        return id; 
    }
    
    public void setId(Long id) { 
        this.id = id; 
    }

    public User getUser() { 
        return user; 
    }
    
    public void setUser(User user) { 
        this.user = user; 
    }

    public Quiz getQuiz() { 
        return quiz; 
    }
    
    public void setQuiz(Quiz quiz) { 
        this.quiz = quiz; 
    }

    public LocalDateTime getAttemptDate() { 
        return attemptDate; 
    }
    
    public void setAttemptDate(LocalDateTime attemptDate) { 
        this.attemptDate = attemptDate; 
    }

    public LocalDateTime getCompletedDate() { 
        return completedDate; 
    }
    
    public void setCompletedDate(LocalDateTime completedDate) { 
        this.completedDate = completedDate; 
    }

    public Double getScore() { 
        return score; 
    }
    
    public void setScore(Double score) { 
        this.score = score; 
    }

    public Boolean getIsPassed() { 
        return isPassed; 
    }
    
    public void setIsPassed(Boolean isPassed) { 
        this.isPassed = isPassed; 
    }

    public Integer getTotalScore() { 
        return totalScore; 
    }
    
    public void setTotalScore(Integer totalScore) { 
        this.totalScore = totalScore; 
    }

    public Integer getFrozenScore() { 
        return frozenScore; 
    }
    
    public void setFrozenScore(Integer frozenScore) { 
        this.frozenScore = frozenScore; 
    }

    public Integer getCurrentLevel() { 
        return currentLevel; 
    }
    
    public void setCurrentLevel(Integer currentLevel) { 
        this.currentLevel = currentLevel; 
    }

    public Integer getCurrentQuestionIndex() { 
        return currentQuestionIndex; 
    }
    
    public void setCurrentQuestionIndex(Integer currentQuestionIndex) { 
        this.currentQuestionIndex = currentQuestionIndex; 
    }

    public Boolean getLifelineFiftyFiftyUsed() { 
        return lifelineFiftyFiftyUsed; 
    }
    
    public void setLifelineFiftyFiftyUsed(Boolean used) { 
        this.lifelineFiftyFiftyUsed = used; 
    }

    public Boolean getLifelineAudiencePollUsed() { 
        return lifelineAudiencePollUsed; 
    }
    
    public void setLifelineAudiencePollUsed(Boolean used) { 
        this.lifelineAudiencePollUsed = used; 
    }

    public Boolean getLifelineSkipQuestionUsed() { 
        return lifelineSkipQuestionUsed; 
    }
    
    public void setLifelineSkipQuestionUsed(Boolean used) { 
        this.lifelineSkipQuestionUsed = used; 
    }

    // Fixed getIsCompleted method - removed duplicate methods
    public Boolean getIsCompleted() { 
        return isCompleted; 
    }
    
    public void setIsCompleted(Boolean isCompleted) { 
        this.isCompleted = isCompleted; 
    }

    public Integer getTimeRemainingSeconds() { 
        return timeRemainingSeconds; 
    }
    
    public void setTimeRemainingSeconds(Integer timeRemainingSeconds) { 
        this.timeRemainingSeconds = timeRemainingSeconds; 
    }

    public List<UserAnswer> getUserAnswers() { 
        return userAnswers; 
    }
    
    public void setUserAnswers(List<UserAnswer> userAnswers) { 
        this.userAnswers = userAnswers; 
    }

    // Business Logic Methods
    public void completeAttempt() {
        this.isCompleted = true;
        this.completedDate = LocalDateTime.now();
    }

    public boolean hasLifelinesRemaining() {
        return !lifelineFiftyFiftyUsed || !lifelineAudiencePollUsed || !lifelineSkipQuestionUsed;
    }

    public int getLifelinesRemaining() {
        int count = 0;
        if (!lifelineFiftyFiftyUsed) count++;
        if (!lifelineAudiencePollUsed) count++;
        if (!lifelineSkipQuestionUsed) count++;
        return count;
    }

    public void freezeScore() {
        this.frozenScore = this.totalScore;
    }

    public void revertToFrozenScore() {
        this.totalScore = this.frozenScore;
    }

    public boolean isSafeZone() {
        return currentLevel == 2 || currentLevel == 4;
    }
    
    private Double percentageScore;
    public Double getPercentageScore() {
        return percentageScore != null ? percentageScore : 0.0;
    }

    public void setPercentageScore(Double percentageScore) {
        this.percentageScore = percentageScore;
    }

}