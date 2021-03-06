package practice.guestregistry.services;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import practice.guestregistry.dao.CardDao;
import practice.guestregistry.exceptions.InvalidDocumentStateException;
import practice.guestregistry.exceptions.ResourceNotFoundException;
import practice.guestregistry.models.Card;

import java.util.List;
import java.util.Optional;

@Service
public class CardService {
    private CardDao cardDao;
    private LocationService locationService;
    private static final Logger log = LoggerFactory.getLogger(CardService.class);

    @Autowired
    public CardService (CardDao cardDao, LocationService locationService) {
        this.cardDao = cardDao;
        this.locationService = locationService; //ar reikia tokio patikrinimo
    }

    public void deleteAll () {
        cardDao.deleteAll();
    }

    public Optional<Card> getCardById (ObjectId id) {
        if (!cardDao.existById(id)) {
            throw new ResourceNotFoundException("Card by this id doesn't exist");
        }
        return cardDao.findById(id);
    }

    public List<Card> getAllCards () {
        return cardDao.findAll();
    }

    public Card addCard (Card newCard) {
        if (validateCardFields(newCard)) {
            return cardDao.save(newCard);
        }
        //TODO:add details?
        throw new InvalidDocumentStateException("Incorrect card details, location must exist in db");
    }

    public void updateCard (Card newCard) {
        if (cardDao.existById(newCard.getId())) {
            if (validateCardFields(newCard)) {
                cardDao.update(newCard);
            } else {
                //TODO:add details?
                throw new InvalidDocumentStateException("Incorrect card details, location must exist in db");
            }
        } else {
            throw new ResourceNotFoundException("Card by this id doesn't exist");
        }
    }

    public void deleteCardById (ObjectId id) {
        if (cardDao.existById(id)) {
            cardDao.deleteById(id);
        } else {
            throw new ResourceNotFoundException("Card by this id doesn't exist");
        }
    }

    //TODO: duplicate NotEmpty? Numbers/Digits?
    //possible date validation
    private boolean validateCardFields(Card card) {
        if (card.getLocation() != null &&
                locationService.getLocationById(card.getLocation().getId()).isPresent()) {
            return true;
        }
        return false;
    }

    public boolean cardExist(Card card) {
        log.trace("card Exist " + card + " " +  cardDao.equals(card));
        return cardDao.exist(card);
    }
}
