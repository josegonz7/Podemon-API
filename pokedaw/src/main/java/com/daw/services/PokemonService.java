package com.daw.services;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.daw.persistence.entities.Pokemon;
import com.daw.persistence.entities.enums.Pokeball;
import com.daw.persistence.entities.enums.Tipo;
import com.daw.persistence.repositories.PokemonRepository;
import com.daw.services.exceptions.PokemonException;
import com.daw.services.exceptions.PokemonNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PokemonService {

	private final PokemonRepository pokemonRepository;

	public List<Pokemon> findAll() {
		return this.pokemonRepository.findAll();
	}

	public Pokemon findById(int idPokemon) {
		if (!this.pokemonRepository.existsById(idPokemon)) {
			throw new PokemonNotFoundException(String.format("El pokemon con id %d no existe. ", idPokemon));
		}

		return this.pokemonRepository.findById(idPokemon).get();
	}

	public Pokemon create(Pokemon pokemon) {
		if (pokemon.getFechaCaptura() != null) {
			throw new PokemonException("No se permite indicar la fecha de captura. ");
		}

		if (pokemon.getTipo1() == null || pokemon.getTipo1().equals(Tipo.NINGUNO)) {
			throw new PokemonException("El tipo1 del pokemon no es válido. ");
		}

		if (pokemon.getTipo2() == null || pokemon.getTipo1().equals(pokemon.getTipo2())) {
			pokemon.setTipo2(Tipo.NINGUNO);
		}

		if (pokemon.getCapturado() == null) {
			pokemon.setCapturado(Pokeball.POKEBALL);
		}

		pokemon.setId(null);
		pokemon.setFechaCaptura(LocalDate.now());

		return this.pokemonRepository.save(pokemon);
	}

	public Pokemon update(int idPokemon, Pokemon pokemon) {
		if (pokemon.getId() == null || pokemon.getId() != idPokemon) {
			throw new PokemonException("El id del body y el id del path no coinciden. ");
		}
		if (!this.pokemonRepository.existsById(idPokemon)) {
			throw new PokemonNotFoundException(String.format("El pokemon con id %d no existe. ", idPokemon));
		}
		if (pokemon.getFechaCaptura() != null) {
			throw new PokemonException("La fecha de captura no se puede modificar. ");
		}

		if (pokemon.getTipo1() == null || pokemon.getTipo1().equals(Tipo.NINGUNO)) {
			throw new PokemonException("El tipo1 del pokemon no es válido. ");
		}
		if (pokemon.getTipo2() == null || pokemon.getTipo1().equals(pokemon.getTipo2())) {
			pokemon.setTipo2(Tipo.NINGUNO);
		}

		Pokemon pokemonBD = this.findById(idPokemon);

		pokemonBD.setNumeroPokedex(pokemon.getNumeroPokedex());
		pokemonBD.setTitulo(pokemon.getTitulo());
		pokemonBD.setTipo1(pokemon.getTipo1());
		pokemonBD.setTipo2(pokemon.getTipo2());
		if (pokemon.getCapturado() != null) {
			pokemonBD.setCapturado(pokemon.getCapturado());
		}

		return this.pokemonRepository.save(pokemonBD);
	}

	public void deleteById(int idPokemon) {
		if (!this.pokemonRepository.existsById(idPokemon)) {
			throw new PokemonNotFoundException(String.format("El pokemon con id %d no existe. ", idPokemon));
		}

		this.pokemonRepository.deleteById(idPokemon);
	}

	public List<Pokemon> findByNumeroPokedex(int numeroPokedex) {
		return this.pokemonRepository.findByNumeroPokedex(numeroPokedex);
	}

	public List<Pokemon> findByRangoFechas(LocalDate fechaInicio, LocalDate fechaFin) {
		if (fechaInicio.isAfter(fechaFin)) {
			throw new PokemonException("La fecha de inicio no puede ser posterior a la fecha de fin. ");
		}
		return this.pokemonRepository.findByFechaCapturaBetween(fechaInicio, fechaFin);
	}

	public List<Pokemon> findByTipo(Tipo tipo) {
		return this.pokemonRepository.findByTipo1OrTipo2(tipo, tipo);
	}

}