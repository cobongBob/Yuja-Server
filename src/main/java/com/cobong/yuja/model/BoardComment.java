package com.cobong.yuja.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.cobong.yuja.model.audit.DateAudit;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@SuppressWarnings("serial")
@Getter
@EqualsAndHashCode(callSuper=false)
@ToString(exclude = {"user","board","parent"})
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class BoardComment extends DateAudit{
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY) //increment
   private Long commentId;
   
   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "boardId")   
   private Board board;
   
   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "userId")   
   private User user;
   
   @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parentId")
    private BoardComment parent;
   
   @OneToMany(mappedBy = "parent", cascade = CascadeType.REMOVE)
   private List<BoardComment> children;
   
   @OneToMany(mappedBy = "comment",cascade = CascadeType.REMOVE)
   @Builder.Default
   private List<Notification> notifications = new ArrayList<Notification>();
   
   //댓글 길이는 설정을 해야함. 
   @Column(nullable = false, length = 2000)
   private String content;
   
   @Column(nullable = false, columnDefinition = "TINYINT(1)")
   private boolean deleted;
   
   public BoardComment createComment(String content, Board board, User user, BoardComment parent) {
      BoardComment comment = new BoardComment();
       comment.content = content;
       comment.board = board;
       comment.user = user;
       comment.parent = parent;
       comment.deleted = false;
       return comment;
    }
   
   public BoardComment modifyComment(String content) {
      this.content = content;
      return this;
   }

   public void deleteComment() {
      this.deleted = true;
   }
   
}