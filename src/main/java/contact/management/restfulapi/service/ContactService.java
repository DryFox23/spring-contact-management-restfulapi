package contact.management.restfulapi.service;

import contact.management.restfulapi.entity.Contact;
import contact.management.restfulapi.entity.User;
import contact.management.restfulapi.model.ContactResponse;
import contact.management.restfulapi.model.CreateContactRequest;
import contact.management.restfulapi.model.SearchContactRequest;
import contact.management.restfulapi.model.UpdateContactRequest;
import contact.management.restfulapi.repository.ContactRepository;
import jakarta.persistence.criteria.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ContactService {

    @Autowired
    private ValidationService validationService;

    @Autowired
    private ContactRepository repository;
    @Autowired
    private ContactRepository contactRepository;

    // service create contact
    @Transactional
    public ContactResponse create(User user, CreateContactRequest request) {
        validationService.validate(request);

        Contact contact = new Contact();
        contact.setId(UUID.randomUUID().toString());
        contact.setFirstName(request.getFirstName());
        contact.setLastName(request.getLastName());
        contact.setEmail(request.getEmail());
        contact.setPhone(request.getPhone());
        contact.setUser(user);
        contactRepository.save(contact);

        return toContactResponse(contact);
    }

    private ContactResponse toContactResponse(Contact contact){
        return ContactResponse.builder()
                .id(contact.getId())
                .firstName(contact.getFirstName())
                .lastName(contact.getLastName())
                .email(contact.getEmail())
                .phone(contact.getPhone())
                .build();
    }

    // Service Get contact by id
    @Transactional(readOnly = true)
    public ContactResponse get(User user, String id){
        Contact contact = contactRepository.findFirstByUserAndId(user, id)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Contact not found"));
        return toContactResponse(contact);
    }

    // Service Update contact
    @Transactional
    public ContactResponse update(User user, UpdateContactRequest request){
        validationService.validate(request);
        Contact contact = contactRepository.findFirstByUserAndId(user, request.getId())
                .orElseThrow(()->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Contact not found"));

        contact.setFirstName(request.getFirstName());
        contact.setLastName(request.getLastName());
        contact.setEmail(request.getEmail());
        contact.setPhone(request.getPhone());
        contactRepository.save(contact);

        return toContactResponse(contact);
    }

    // Serivce Delete contact
    @Transactional
    public void delete(User user, String contactId){
        Contact contact = contactRepository.findFirstByUserAndId(user, contactId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Contact not found"));

            contactRepository.delete(contact);
    }

    // Service Search contact
    @Transactional(readOnly = true)
    public Page<ContactResponse> search(User user, SearchContactRequest request) {
        Specification<Contact> specification = (root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(builder.equal(root.get("user"), user));
            if (Objects.nonNull(request.getName())) {
                predicates.add(builder.or(
                        builder.like(root.get("firstName"), "%" + request.getName() + "%"),
                        builder.like(root.get("lastName"), "%" + request.getName() + "%")
                ));
            }

            if (Objects.nonNull((request.getEmail()))) {
                predicates.add(builder.like(root.get("email"), "%" + request.getEmail() + "%"));
            }

            if (Objects.nonNull((request.getPhone()))) {
                predicates.add(builder.like(root.get("phone"), "%" + request.getPhone() + "%"));
            }

            return query.where(predicates.toArray((new Predicate[]{}))).getRestriction();
        };

        Pageable pageable = PageRequest.of(request.getPage(), request.getSize());
        Page<Contact> contacts = contactRepository.findAll(specification, pageable);

        List<ContactResponse> contactResponse = contacts.getContent().stream()
                .map(this::toContactResponse)
                .toList();

        return new PageImpl<>(contactResponse, pageable, contacts.getTotalElements());
    }
}
