package org.example.Controller;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import org.example.Model.Comment_reactions;
import org.example.Model.Comments;
import org.example.Model.Post;
import org.example.Model.Post_reactions;
import org.example.Service.*;
import org.example.Util.DatabaseConnection;
import javafx.scene.input.MouseEvent;
import java.sql.Connection;
import java.util.List;



public class PostItemController {
    Connection cnx2;
    public PostItemController() {cnx2  = DatabaseConnection.getInstance().getCnx();}
    TranslationService translationService = new TranslationService();
    @FXML private Label titleLabel;
    @FXML private Label contentLabel;
    @FXML private Label categoryLabel;
    @FXML private ImageView postImage;
    @FXML private TextField input_comment;
    @FXML private VBox commentsContainer;
    @FXML private Button showCommentsButton;
    @FXML private HBox commentInputContainer;
    @FXML private Button commenter;
    @FXML private Button reactionButton;
    @FXML private HBox reactionMenu;
    int currentUserId = 1 ;
    private Post post; // Stocke les données du post
    private Comments comment;
    CommentService commentService = new CommentService();
    private boolean commentsVisible = false;
    private boolean isOverReactionMenu = false;
    private boolean isOverReactionMenuComment = false;
    private boolean response_input = false ;
    private boolean ResponsesVis = false ;
    private boolean isOptionsMenuVisible = false;
    @FXML private Button optionsButton;
@FXML private VBox optionsMenu;
@FXML private Button modifyButton;
@FXML private Button deleteButton;

    @FXML
public void initialize() {
            // Configuration du menu options
            optionsButton.setOnAction(event -> toggleOptionsMenu());

            // Gestionnaires pour les boutons
            modifyButton.setOnAction(event -> handleEditPost());
            deleteButton.setOnAction(event -> handleDeletePost());

            // Cacher le menu quand on clique ailleurs
            optionsMenu.setOnMouseExited(event -> {
                    new Thread(() -> {
                            try {
                                    Thread.sleep(500);
                                    Platform.runLater(() -> optionsMenu.setVisible(false));
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                        }).start();
                });
        }
    @FXML
    private void handleDeletePost() {
        PostService postService = new PostService();
        postService.PostDelete(post.getId());

    }
    @FXML
    private void handleEditPost() {
        PostService postService = new PostService();
        postService.PostUp(post.getId());
    }

    private void toggleOptionsMenu() {
           isOptionsMenuVisible = !isOptionsMenuVisible;
            optionsMenu.setVisible(isOptionsMenuVisible);
    }

    @FXML
    private void toggleCommentInput() {
                // Inverse la visibilité du conteneur de commentaire
               commentInputContainer.setVisible(!commentInputContainer.isVisible());

                // Change le texte du bouton selon l'état
                if (commentInputContainer.isVisible()) {
                        commenter.setText("Annuler");
                        input_comment.requestFocus();
                    } else {
                        commenter.setText("commenter");
                        input_comment.clear();
                    }
            }
            BadwordService BadwordService = new BadwordService();
    private static final String TITLE_STYLE = "title";
    @FXML
    private void handleAddComment() {
        if (input_comment.getText() == null || input_comment.getText().trim().isEmpty()) {
            // Afficher une alerte
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur de saisie");
            alert.setHeaderText(null);
            alert.setContentText("Le commentaire ne peut pas être vide!");
            alert.showAndWait();
            return;
        }
        boolean bb = BadwordService.containsBadwords(input_comment.getText().trim());

        if (bb) {
            showAlert("Contenu inapproprié", "Votre commentaire contient des mots inappropriés. Veuillez le modifier.");
            // Optionnel: mettre en surbrillance les mots inappropriés
            input_comment.setStyle("-fx-border-color: red;");
            return;
        }

        int post_id = post.getId();
        comment = new Comments(post_id,currentUserId, input_comment.getText());
        commentService.AddComment(comment);
        input_comment.clear();
        loadComments();
    }
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    private double initialHeight = -1;
    @FXML private HBox postContainer;
    @FXML private Button tri ;
    @FXML
    private void toggleComments() {
        if (!commentsVisible) {
                        // Sauvegarder la hauteur initiale si pas encore fait
                        if (initialHeight == -1) {
                                initialHeight = postContainer.getHeight();
                            }

                        // Afficher les commentaires
                        loadComments();
                        commentsContainer.setVisible(true);
                        commentsContainer.setManaged(true);
                        showCommentsButton.setText("Masquer Commentaires");

                        // Animation pour agrandir
                        Timeline timeline = new Timeline(
                                    new KeyFrame(Duration.millis(300),
                                        new KeyValue(postContainer.prefHeightProperty(),
                                            postContainer.getHeight() + commentsContainer.getHeight())
                            )
                        );
                        timeline.play();

                    } else {
                        // Animation pour réduire
                        Timeline timeline = new Timeline(
                                    new KeyFrame(Duration.millis(300),
                                        new KeyValue(postContainer.prefHeightProperty(), initialHeight)
                            )
                        );

                        timeline.setOnFinished(e -> {
                                commentsContainer.setVisible(false);
                                commentsContainer.setManaged(false);
                                postContainer.setPrefHeight(initialHeight);
                            });

                        timeline.play();
                        showCommentsButton.setText("Afficher Commentaires");
                    }

                commentsVisible = !commentsVisible;
            }

    private void loadComments() {
        commentsContainer.getChildren().clear();
        List<Comments> comments = commentService.getCommentsByPostId(this.post.getId());

        for (Comments comment : comments) {
            HBox commentRow = new HBox(10);
            commentRow.getStyleClass().add("comment-row");

            VBox textContainer = new VBox(5);

            // Username label
            Label usernameLabel = new Label("comment.getUsername()"); // Assuming you have a method to get the username
            usernameLabel.getStyleClass().add("username-label");

            // Comment text
            Label contentLabel = new Label(comment.getComment_text());
            contentLabel.getStyleClass().add("comment-text");
            contentLabel.setWrapText(true);

            // Comment date
            Label dateLabel = new Label(comment.getDate());
            dateLabel.setStyle("-fx-text-fill: #65676b; -fx-font-size: 11px;");



            textContainer.getChildren().addAll(usernameLabel, contentLabel, dateLabel);

            HBox buttonContainer = new HBox(5);
            Button editBtn = new Button("Modifier");
            Button deleteBtn = new Button("Supprimer");

            editBtn.getStyleClass().add("comment-button");
            deleteBtn.getStyleClass().add("comment-button");

            editBtn.setOnAction(e -> handleEditSpecificComment(comment.getComment_id()));
            deleteBtn.setOnAction(e -> handleDeleteSpecificComment(comment.getComment_id()));

            buttonContainer.getChildren().addAll(editBtn, deleteBtn);

            ImageView avatarView = new ImageView();
            avatarView.setFitWidth(32);
            avatarView.setFitHeight(32);
            avatarView.getStyleClass().add("avatar-image");

            try {
                Image avatarImage = new Image("/images/p.png");
                avatarView.setImage(avatarImage);
            } catch (Exception e) {
                e.printStackTrace();
            }

            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.ALWAYS);

            commentRow.getChildren().addAll(
                    avatarView,
                    textContainer,
                    spacer,
                    buttonContainer
            );

            HBox reactionButtons = new HBox(15) ;
            reactionButtons.getStyleClass().add("interaction-buttons");
            reactionButtons.setStyle("-fx-padding : 0 0 0 42;");
// button reaction commentaire :
            Button likeBtn = new Button("j'aime");
            likeBtn.getStyleClass().add("comment-interaction-button");

            HBox reactionMenuComment = new HBox(5);
            reactionMenuComment.getStyleClass().add("reaction-menu");
            reactionMenuComment.setVisible(false);

            //emoji
            Label likeEmoji = new Label("👍");
            Label loveEmoji = new Label ("❤\uFE0F");
            Label hahaEmoji = new Label ("\uD83D\uDE02");
            Label clapEmoji = new Label ("\uD83D\uDC4F");
            Label wowEmoji = new Label ("\uD83D\uDE2E");
            Label sadEmoji = new Label ("\uD83D\uDE22");

            likeEmoji.getStyleClass().add("reaction-emoji");
            loveEmoji.getStyleClass().add("reaction-emoji");
            hahaEmoji.getStyleClass().add("reaction-emoji");
            clapEmoji.getStyleClass().add("reaction-emoji");
            wowEmoji.getStyleClass().add("reaction-emoji");
            sadEmoji.getStyleClass().add("reaction-emoji");

            // click emoji :
            likeEmoji.setOnMouseClicked(e -> handleReactionComment(comment.getComment_id(),"👍",likeBtn));
            loveEmoji.setOnMouseClicked(e -> handleReactionComment(comment.getComment_id(),"❤\uFE0F",likeBtn));
            hahaEmoji.setOnMouseClicked(e -> handleReactionComment(comment.getComment_id(),"\uD83D\uDE02",likeBtn));
            clapEmoji.setOnMouseClicked(e -> handleReactionComment(comment.getComment_id(),"\uD83D\uDC4F",likeBtn));
            wowEmoji.setOnMouseClicked(e  -> handleReactionComment(comment.getComment_id(),"\uD83D\uDE2E",likeBtn));
            sadEmoji.setOnMouseClicked(e ->  handleReactionComment(comment.getComment_id(),"\uD83D\uDE22",likeBtn));

             reactionMenuComment.getChildren().addAll(likeEmoji,loveEmoji,hahaEmoji,clapEmoji,wowEmoji,sadEmoji);

             likeBtn.setOnMouseEntered(e-> showReactionMenuComment(reactionMenuComment));
             likeBtn.setOnMouseExited(e-> hideReactionMenuComment(reactionMenuComment));
             reactionMenuComment.setOnMouseEntered(e -> showReactionMenuComment(reactionMenuComment));
             reactionMenuComment.setOnMouseExited( e -> hideReactionMenuComment(reactionMenuComment));



            Button replyBtn = new Button("repondre");
            replyBtn.getStyleClass().add("comment-interaction-button");
            TextField inputResponse = new TextField("ecrire votre reponse ");
            inputResponse.setVisible(response_input);
            Button validResponse = new Button("Repondre");
            validResponse.getStyleClass().add("send-button");
            validResponse.setVisible(response_input);
            replyBtn.setOnAction(e -> {
                response_input = !response_input;
                inputResponse.setVisible(response_input);
                validResponse.setVisible(response_input);

            });
            validResponse.setOnAction(e -> handleValidResponse(inputResponse.getText() , comment.getComment_id()));


         Button showRep = new Button("Voir les reponses ");
         showRep.getStyleClass().add("comment-interaction-button");

         Button Translate = new Button("Traduire");
         Translate.getStyleClass().add("comment-interaction-button");
         Translate.setOnAction(e -> translationService.autoTranslateAndUpdateLabel(contentLabel,comment.getComment_text()));

         VBox responses_container = new VBox();
         responses_container.getStyleClass().add("comment-row"); // style -----------!!!!!!!!!!!!!!!!!
            responses_container.setVisible(false);
            showRep.setOnAction(e -> ShowResponses(responses_container,comment.getComment_id()));


            inputResponse.getStyleClass().add("comment-input-field");







            likeBtn.setStyle("-fx-font-size: 11px; -fx-background-color: transparent; -fx-text-fill: #65676b; -fx-cursor: hand;");
            replyBtn.setStyle("-fx-font-size: 11px; -fx-background-color: transparent; -fx-text-fill: #65676b; -fx-cursor: hand;");
            showRep.setStyle("-fx-font-size: 11px; -fx-background-color: transparent; -fx-text-fill: #65676b; -fx-cursor: hand;");
            Translate.setStyle("-fx-font-size: 11px; -fx-background-color: transparent; -fx-text-fill: #65676b; -fx-cursor: hand;");
            reactionButtons.getChildren().addAll(likeBtn,replyBtn,showRep,Translate);


            commentsContainer.getChildren().addAll(commentRow , reactionButtons , reactionMenuComment , inputResponse , validResponse , responses_container );
        }
    }





    public void ShowResponses(VBox responses_container , int comment_id){
        ResponsesVis = !ResponsesVis;
        if ( ResponsesVis){
            responses_container.getChildren().clear();
            loadResponses(responses_container , comment_id);
            responses_container.setVisible(ResponsesVis);
            responses_container.setManaged(ResponsesVis);
        }else {
            responses_container.setVisible(ResponsesVis);
            responses_container.setManaged(ResponsesVis);
        }


    }
    public void loadResponses(VBox responses_container ,int comment_id){
        List <Comments> responses = commentService.showResponses(comment_id);
        for (Comments response : responses) {
            // Username label
            Label usernameLabel = new Label("comment.getUsername()"); // Assuming you have a method to get the username
            usernameLabel.getStyleClass().add("username-label");

            // Comment text
            Label contentLabel = new Label(response.getComment_text());
            contentLabel.getStyleClass().add("comment-text");
            contentLabel.setWrapText(true);

            // Comment date
            Label dateLabel = new Label(response.getDate());
            dateLabel.setStyle("-fx-text-fill: #65676b; -fx-font-size:6px;");

            responses_container.getChildren().addAll(usernameLabel , contentLabel , dateLabel);
        }

    }

    public void handleValidResponse(String input , int id ){
        Comments response = new Comments(post.getId() , currentUserId , input , id);
        CommentService commentService = new CommentService();
        commentService.AddResponse(response);

    }
    public void showReactionMenuComment(HBox reactionMenuComment) {
        isOverReactionMenuComment = true ;
        reactionMenuComment.setVisible(true);
        reactionMenuComment.setManaged(true);
    }
    public void hideReactionMenuComment(HBox reactionMenuComment) {
        isOverReactionMenuComment = false ;
        new Thread(() -> {
            try {
                Thread.sleep(200);
                if (!isOverReactionMenuComment) {
                    Platform.runLater(() -> {
                        reactionMenuComment.setVisible(false);
                        reactionMenuComment.setManaged(false);
                    });
                }
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }).start();

    }

    public void handleReactionComment(int comment_id ,String reaction , Button likeBtn) {
        int currentUserId = 1;
        ReactionCommentService reactionCommentService = new ReactionCommentService();
        Comment_reactions comment_reaction = new Comment_reactions(comment_id, currentUserId , reaction);
        String reaction_text = getReactionText(reaction);
        Comment_reactions existing = reactionCommentService.getReactionByUser(currentUserId , comment_id);
        if (existing == null){
            likeBtn.setText(reaction + reaction_text );
            reactionCommentService.Reaction_add(comment_reaction);
        }else if (existing.getReaction().equals(reaction)){
            reactionCommentService.Reaction_Delete(existing.getComment_id(),currentUserId);
            likeBtn.setText("j'aime");
        }else {
            reactionCommentService.Reaction_Up(existing.getComment_id(),currentUserId ,reaction);
            likeBtn.setText(reaction + reaction_text);
        }


    }
    public void setPostData(Post post) {
        if (post == null) return;

        this.post = post;
        titleLabel.setText(post.getTitle() != null ? post.getTitle() : "");
        contentLabel.setText(post.getContent() != null ? post.getContent() : "");
        categoryLabel.setText(post.getCategory() != null ? post.getCategory() : "");

        // Vérification de l'image
        if (post.getImage() != null && !post.getImage().isEmpty()) {
            try {
                postImage.setImage(new Image(post.getImage()));
                postImage.setVisible(true);
            } catch (Exception e) {
                postImage.setVisible(false);
            }
        } else {
            postImage.setVisible(false);
        }
    }


    @FXML
    public void onReactionMenuExited()  {
        isOverReactionMenu = false;
        // Petit délai pour permettre une transition fluide
        new Thread(() -> {
            try {
                Thread.sleep(200);
                if (!isOverReactionMenu) {
                    Platform.runLater(() -> reactionMenu.setVisible(false));
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }
    @FXML
    private void onReactionMenuEntered() {
            isOverReactionMenu = true;
            reactionMenu.setVisible(true);
        }

    @FXML
    private void handleReactionPost(MouseEvent event) {
        Label emojiLabel = (Label) event.getSource();
        String newEmoji = emojiLabel.getText();


        int currentUserId = 1; // À remplacer par l'ID réel de l'utilisateur

        ReactionService reactionService = new ReactionService();
        Post_reactions existingReaction = reactionService.getUserReaction(post.getId(), currentUserId);

        if (existingReaction == null) {
            // Pas de réaction existante -> Ajouter nouvelle réaction
            Post_reactions newReaction = new Post_reactions(post.getId(), currentUserId, getReactionText(newEmoji));
            reactionService.Reaction_add(newReaction);
            reactionButton.setText(newEmoji + " " +getReactionText(newEmoji));
        } else if (existingReaction.getReaction().equals(getReactionText(newEmoji))) {
            // Même réaction -> Supprimer la réaction
            reactionService.Reaction_delete(post.getId(), currentUserId);

        } else {
            // Réaction différente -> Mettre à jour la réaction
            Post_reactions updatedReaction = new Post_reactions(post.getId(), currentUserId, getReactionText(newEmoji));
            reactionService.Reaction_update(updatedReaction);
            reactionButton.setText(newEmoji + " " + getReactionText(newEmoji));
        }

        reactionMenu.setVisible(false);
    }
    private String getReactionText(String emoji) {
        switch (emoji) {
            case "👍":
                return "J'aime";
            case "❤️":
                return "J'adore";
            case "😂":
                return "Haha";
            case "😮":
                return "Wow";
            case "😢":
                return "Triste";
            case "\uD83D\uDC4F":
                return "bravo";
            default:
                return "";
        }
    }

    private void handleDeleteSpecificComment(int commentId) {
        commentService.DeleteSpecificComment(commentId);
        loadComments(); // Recharger les commentaires après la suppression
    }
    private void handleEditSpecificComment(int commentId) {
        // Créer une boîte de dialogue pour la modification
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Modifier le commentaire");
        dialog.setHeaderText(null);
        dialog.setContentText("Entrez le nouveau commentaire:");

        dialog.showAndWait().ifPresent(newText -> {
            commentService.UpdateSpecificComment(commentId, newText);
            loadComments(); // Recharger les commentaires après la modification
        });
    }




}
