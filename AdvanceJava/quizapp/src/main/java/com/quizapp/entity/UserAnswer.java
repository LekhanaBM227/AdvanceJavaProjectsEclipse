package com.quizapp.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_answers")
public class UserAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quiz_attempt_id", nullable = false)
    private QuizAttempt quizAttempt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "selected_option_id")
    private Option selectedOption;

    @Column(name = "is_correct")
    private Boolean isCorrect = false;

    @Column(name = "is_skipped")
    private Boolean isSkipped = false;

    @Column(name = "time_taken_seconds")
    private Integer timeTakenSeconds;

    @Column(name = "points_earned")
    private double pointsEarned = 0;

    @Column(name = "answered_at")
    private LocalDateTime answeredAt;

    // Constructors
    public UserAnswer() {
        this.answeredAt = LocalDateTime.now();
    }

    public UserAnswer(QuizAttempt quizAttempt, Question question) {
        this();
        this.quizAttempt = quizAttempt;
        this.question = question;
    }

    public UserAnswer(QuizAttempt quizAttempt, Question question, Option selectedOption) {
        this(quizAttempt, question);
        setSelectedOption(selectedOption); // sets correctness and points
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public QuizAttempt getQuizAttempt() {
        return quizAttempt;
    }

    public void setQuizAttempt(QuizAttempt quizAttempt) {
        this.quizAttempt = quizAttempt;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public Option getSelectedOption() {
        return selectedOption;
    }

    public void setSelectedOption(Option selectedOption) {
        this.selectedOption = selectedOption;
        if (selectedOption != null && question != null) {
            this.isCorrect = question.isCorrectOption(selectedOption.getId());
            this.pointsEarned = this.isCorrect ? question.getPoints() : 0;
            this.isSkipped = false;
        } else {
            this.isCorrect = false;
            this.pointsEarned = 0;
            this.isSkipped = true;
        }
    }

    public Boolean getIsCorrect() {
        return isCorrect;
    }

    public void setIsCorrect(Boolean isCorrect) {
        this.isCorrect = isCorrect;
    }

    public Boolean getIsSkipped() {
        return isSkipped;
    }

    public void setIsSkipped(Boolean isSkipped) {
        this.isSkipped = isSkipped;
        if (isSkipped) {
            this.selectedOption = null;
            this.isCorrect = false;
            this.pointsEarned = 0;
        }
    }

    public Integer getTimeTakenSeconds() {
        return timeTakenSeconds;
    }

    public void setTimeTakenSeconds(Integer timeTakenSeconds) {
        this.timeTakenSeconds = timeTakenSeconds;
    }

    public double getPointsEarned() {
        return pointsEarned;
    }

    public void setPointsEarned(Integer pointsEarned) {
        this.pointsEarned = pointsEarned;
    }

    public LocalDateTime getAnsweredAt() {
        return answeredAt;
    }

    public void setAnsweredAt(LocalDateTime answeredAt) {
        this.answeredAt = answeredAt;
    }

	public void setSelectedOption(String userAnswer) {
		// TODO Auto-generated method stub
		
	}
}
