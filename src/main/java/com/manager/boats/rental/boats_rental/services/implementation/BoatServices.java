package com.manager.boats.rental.boats_rental.services.implementation;

import java.util.List;
import java.util.Comparator;
import java.util.Date;
import java.util.Optional;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.manager.boats.rental.boats_rental.persistence.models.Boat;
import com.manager.boats.rental.boats_rental.persistence.models.EstateRental;
import com.manager.boats.rental.boats_rental.persistence.models.Marin;
import com.manager.boats.rental.boats_rental.persistence.models.Rental;
import com.manager.boats.rental.boats_rental.repositories.IBoatRepository;
import com.manager.boats.rental.boats_rental.repositories.IMarinRepository;
import com.manager.boats.rental.boats_rental.repositories.IRentalRepository;
import com.manager.boats.rental.boats_rental.services.exception.NotFoundException;
import com.manager.boats.rental.boats_rental.services.interfaces.IBoatServices;
import com.manager.boats.rental.boats_rental.web.controller.dto.BoatResponse;
import com.manager.boats.rental.boats_rental.web.controller.dto.MarinResponse;
import com.manager.boats.rental.boats_rental.web.controller.dto.RentalDto;

import org.springframework.transaction.annotation.Transactional;

@Service
public class BoatServices implements IBoatServices{
    @Autowired
    private IBoatRepository boatRepository;

    @Autowired
    private IMarinRepository marinRepository;

    @Autowired
    private IRentalRepository rentalRepository;

    @Autowired
    private ModelMapper modelMapper;


    @Transactional
    @Override
    public void delete(Long tuition) {
    Optional<Boat> boatOptional = boatRepository.findByTuition(tuition);
    if (boatOptional.isPresent()) {
        Boat originalBoat = boatOptional.get();

        // Obtener la lista de alquileres asociados al barco **que no estén cancelados**
        // es decir que si todas sus rentas fueron canceladas podemos eliminarlo sin problema
        // en caso que tenga una renta en estado pendiente o confirmada lo elimnare sin problema
        List<Rental> rentals = originalBoat.getRentals()
                .stream()
                .filter(rental->rental.getState() != EstateRental.CANCELADO && rental.getState() != EstateRental.FINALIZADA)
                .toList();

        if (!rentals.isEmpty()) { // Verificar si existen alquileres asociados
            for (Rental rental : rentals) {
                List<Boat> substituteBoats = boatRepository.findAll()
                        .stream()
                        .filter(boat -> boat.getType().equals(originalBoat.getType()) &&
                                boat.getAbility() >= originalBoat.getAbility() &&
                                boat.getPriceHours() <= originalBoat.getPriceHours() &&
                                boat.isEnabled() &&
                                isBoatAvailable(boat, rental.getDateInit(), rental.getDateEnd()))
                        .sorted(Comparator.comparing(Boat::getPriceHours))
                        .toList();

                if (!substituteBoats.isEmpty()) {
                    Boat substituteBoat = substituteBoats.get(0);
                    rental.setBoat(substituteBoat);
                    rentalRepository.save(rental);
                    // ... (código para informar al usuario sobre la reasignación)
                } else {
                    // IllegalStateException es mas especifico indica problema conn los datos de entrada enviados.
                    throw new IllegalStateException("No se puede eliminar el barco. No hay sustitutos disponibles para el alquiler " + rental.getId());
                }
            }
        }


        Marin marin = originalBoat.getMarin();
        if (marin != null) {
            marin.getBoats().remove(originalBoat);
            marinRepository.save(marin);
        }

        boatRepository.delete(originalBoat);


    } else {
        throw new NotFoundException("Not found");
    }
}
    // Método para verificar disponibilidad (adaptar a tu lógica)
    private boolean isBoatAvailable(Boat boat, Date dateInit, Date dateEnd) {
        List<Rental> rentals = rentalRepository.findRentalsByBoatAndDateRange(boat, dateInit, dateEnd)
        .stream()
        .filter(rental -> rental.getState() != EstateRental.CANCELADO && rental.getState() != EstateRental.FINALIZADA)
        .toList();
        return rentals.isEmpty();
    }

    @Transactional(readOnly = true)
    @Override
    public List<BoatResponse> getAll() {
        List<Boat> boats = boatRepository.findAll();

        return boats.stream()
                .map(boat -> {
                    BoatResponse boatDTO = modelMapper.map(boat, BoatResponse.class);

                    if (boat.getMarin() != null) {
                        boatDTO.setMarin(modelMapper.map(boat.getMarin(), MarinResponse.class));
                    }

                    List<RentalDto> rentalDTOs = boat.getRentals().stream()
                            .map(rental -> {
                                RentalDto rentalDTO = modelMapper.map(rental, RentalDto.class);
                                return rentalDTO;
                            })
                            .toList();
                    boatDTO.setRentals(rentalDTOs);

                    return boatDTO;
                })
                .toList();
    }

    @Transactional(readOnly = true)
    @Override
    public BoatResponse getByTuition(Long tuition) {
        Optional<Boat> boat = boatRepository.findByTuition(tuition);
        if(boat.isPresent()){
            return modelMapper.map(boat.get(), BoatResponse.class);
        }else{
            throw new NotFoundException("Not found");
        }
        
    }
    
    @Transactional(readOnly = true)
    @Override
    public List<BoatResponse> getBoatsAvaiable(String dateInit,String dateEnd) throws ParseException{
        SimpleDateFormat stdDateFormat = new SimpleDateFormat("yyyy-MM-dd");

        Date dnInit = stdDateFormat.parse(dateInit);
        Date dnEnd = stdDateFormat.parse(dateEnd);
        return boatRepository.getBoatsAvaliable(dnInit,dnEnd).stream()
        .map(boat -> modelMapper.map(boat, BoatResponse.class))
        .toList();

    }

    @Transactional
    @Override
    public void save(Boat boat) {
        if (boatRepository.findByTuition(boat.getTuition()).isPresent()) {
            throw new RuntimeException("A boat with this tuition already exists.");
        }
        
            Boat newBoat = new Boat();
            newBoat.setAbility(boat.getAbility());
            newBoat.setModel(boat.getModel());
            newBoat.setName(boat.getName());
            newBoat.setType(boat.getType());
            newBoat.setTuition(boat.getTuition());
            // newBoat.setState("aviable");
            newBoat.setPriceHours(boat.getPriceHours());
            boatRepository.save(newBoat);
            
        }
    
    @Transactional
    @Override
    public void updateBoat(Boat boat, Long tuition) {
            Optional<Boat> existingBoatOptional = boatRepository.findByTuition(tuition);
        
            if (existingBoatOptional.isPresent()) {
                Boat existingBoat = existingBoatOptional.get();
                existingBoat.setAbility(boat.getAbility());
                existingBoat.setModel(boat.getModel());
                existingBoat.setName(boat.getName());
                existingBoat.setType(boat.getType());
                existingBoat.setTuition(boat.getTuition());
                // existingBoat.setState(boat.getState());
                existingBoat.setPriceHours(boat.getPriceHours());
        
                boatRepository.save(existingBoat); // Save the updated boat
            } else {
                throw new NotFoundException("Boat with ID " + tuition + " not found.");
            }
    }
    

    @Transactional(readOnly = true)
    @Override
    public boolean existsByTuition(Long tuition) {
        return boatRepository.existsById(tuition);
    }

    @Transactional
    @Override
    public void insertMarinInBoat(Long marinId, Long boatId) {
        Optional<Boat> boat = boatRepository.findByTuition(boatId);
        Optional<Marin> marin = marinRepository.findById(marinId);

        if (marin.isEmpty()) {
            throw new NotFoundException("Marin not found");
        }

        if (boat.isEmpty()) {
            throw new NotFoundException("Boat not found");
        }

        // Si ambos existen, procede con la actualización
        Marin marinDb = marin.get();
        marinDb.getBoats().add(boat.get());
        marinRepository.save(marinDb);

        Boat boatDb = boat.get();
        boatDb.setMarin(marin.get());
        boatRepository.save(boatDb);
    }

}


// @Transactional
// @Override
// public void updateProduct(Boat boat, Long id) {
//     Boat existingBoat = boatRepository.findById(id)
//             .orElseThrow(() -> new NotFoundException("Boat not found"));

//     // Obtener la capacidad máxima requerida por las rentas existentes
//      int maxCapacityForExistingRentals = existingBoat.getRentals().stream()
//             .filter(rental -> rental.getState() == EstateRental.CONFIRMADA || rental.getState() == EstateRental.PENDIENTE) // Filtrar por rentas confirmadas o pendientes
//             .mapToInt(Rental::getPassengerQuantity)
//             .max().orElse(0); // Si no hay rentas, el máximo es 0


//     if (boat.getCapacity() < maxCapacityForExistingRentals) {
//         throw new IllegalStateException("Cannot reduce capacity below existing confirmed/pending rental requirements. The minimum capacity should be " + maxCapacityForExistingRentals);
//     }

//     // Actualizar el bote si la nueva capacidad es válida
//     existingBoat.setCapacity(boat.getCapacity());
//     // ... Actualizar otros campos del bote ...
//     boatRepository.save(existingBoat);
// }