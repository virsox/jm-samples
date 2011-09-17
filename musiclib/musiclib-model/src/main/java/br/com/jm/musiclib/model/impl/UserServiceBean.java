package br.com.jm.musiclib.model.impl;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.inject.Inject;

import org.bson.types.ObjectId;

import br.com.jm.musiclib.model.Music;
import br.com.jm.musiclib.model.User;
import br.com.jm.musiclib.model.UserService;
import br.com.jm.musiclib.model.cdi.UserCollection;
import br.com.jm.musiclib.model.converter.Converter;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.WriteResult;

/**
 * Implementação EJB do serviço de manipulação de usuários.
 * @author Paulo Sigrist / Wilson A. Higashino
 */
@Stateless
@Local(UserService.class)
public class UserServiceBean implements UserService {

  /** Coleção de usuários. */
  @Inject
  @UserCollection
  protected DBCollection userColl;

  /** Conversor de objetos User. */
  @Inject
  protected Converter<User> userConv;

  /** {@inheritDoc} */
  @Override
  public User login(String login, String password) {

    DBObject loginQuery = new BasicDBObject();
    loginQuery.put("login", login);
    loginQuery.put("password", password);

    DBObject result = this.userColl.findOne(loginQuery);
    if (result != null) { return userConv.toObject(result); }
    return null;
  }

  /** {@inheritDoc} */
  @Override
  public void play(User user, Music music) {

    DBObject key = new BasicDBObject("_id", new ObjectId(user.getId()));
    key.put("executions.music", new ObjectId(music.getId()));

    DBObject update = new BasicDBObject("$inc", new BasicDBObject(
        "executions.$.quantity", 1));
    this.userColl.update(key, update);
  }

  /** {@inheritDoc} */
  @Override
  public void update(User user) {//, Playlist playlist) {

    DBObject key = new BasicDBObject("_id", new ObjectId(user.getId()));
    this.userColl.update(key, userConv.toDBObject(user));
  }

  /** {@inheritDoc} */
  public String createUser(User user) {

    DBObject doc = userConv.toDBObject(user);

    WriteResult result = userColl.insert(doc);
    if (result.getError() != null) { throw new RuntimeException(
        "Erro na criação de usuário/"); }

    ObjectId id = (ObjectId) doc.get("_id");
    user.setId(id.toString());
    return user.getId();
  }

}
