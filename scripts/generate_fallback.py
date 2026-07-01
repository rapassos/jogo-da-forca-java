from __future__ import annotations

import json
import os
import re
import sys
import time
from typing import Any
from urllib.parse import quote

import requests

# Configurações alinhadas com o Enum de Dificuldade do Java
LIMITS = {
    "facil": {"min": 3, "max": 5},
    "medio": {"min": 6, "max": 8},
    "dificil": {"min": 9, "max": 11},
    "extremo": {"min": 12, "max": 30},
}
TARGET_PER_LEVEL = 300
OUTPUT_PATH = "src/main/resources/palavras.json"

API_RANDOM = "https://api.dicionario-aberto.net/random"
API_WORD = "https://api.dicionario-aberto.net/word/"
HEADERS = {"User-Agent": "ForcaSwingApp/1.0 (Python Dataset Generator)"}


def clean_xml(raw_xml: str | None) -> str:
    """Remove as tags XML da definição, igualzinho ao código Java."""
    if not raw_xml:
        return "Significado indisponível."
    clean = re.sub(r"<[^>]*>", " ", raw_xml)
    clean = re.sub(r"\s+", " ", clean)
    return clean.strip()


def get_word_definition(word: str) -> str:
    """Busca o significado da palavra na API."""
    try:
        url = f"{API_WORD}{quote(word.lower())}"
        response = requests.get(url, headers=HEADERS, timeout=5)
        if response.status_code == 200:
            data = response.json()
            if isinstance(data, list) and len(data) > 0:
                xml_content = data[0].get("xml", "")
                return clean_xml(xml_content)
    except (requests.RequestException, ValueError):
        return "Significado indisponível."
    return "Significado indisponível."


def load_existing_data() -> dict[str, list[dict[str, Any]]]:
    """Carrega dados se o script já tiver sido rodado antes, evitando recomeçar do zero."""
    if os.path.exists(OUTPUT_PATH):
        try:
            with open(OUTPUT_PATH, "r", encoding="utf-8") as handle:
                return json.load(handle)
        except (OSError, json.JSONDecodeError):
            pass
    return {level: [] for level in LIMITS}


def save_dataset(dataset: dict[str, list[dict[str, Any]]]) -> None:
    """Persiste o dataset no formato JSON esperado pelo projeto."""
    with open(OUTPUT_PATH, "w", encoding="utf-8") as handle:
        json.dump(dataset, handle, ensure_ascii=False, indent=2)


def main() -> int:
    dataset = load_existing_data()
    total_requests = 0

    print("🚀 Iniciando a extração de palavras da API Dicionário Aberto...")

    try:
        while any(len(dataset[level]) < TARGET_PER_LEVEL for level in LIMITS):
            response = requests.get(API_RANDOM, headers=HEADERS, timeout=5)
            total_requests += 1

            if response.status_code != 200:
                print(f"⚠️ API retornou status {response.status_code}. Aguardando 5 segundos...")
                time.sleep(5)
                continue

            payload = response.json()
            word = str(payload.get("word", "")).strip()
            word_lower = word.lower()
            length = len(word_lower)

            if "-" in word_lower or " " in word_lower or not word_lower.isalpha():
                continue

            matched_level = next(
                (level for level, rules in LIMITS.items() if rules["min"] <= length <= rules["max"]),
                None,
            )
            if not matched_level:
                continue

            current_list = dataset[matched_level]
            if len(current_list) < TARGET_PER_LEVEL and not any(item.get("word") == word_lower for item in current_list):
                definition = get_word_definition(word_lower)
                current_list.append({"word": word_lower, "definition": definition})
                print(f"✅ [{matched_level.upper()}] Adicionada: '{word_lower}' ({len(current_list)}/{TARGET_PER_LEVEL})")
                save_dataset(dataset)

            time.sleep(0.15)

            if total_requests % 100 == 0:
                print(f"📊 Status atual -> Requisições feitas: {total_requests}")
                for level in LIMITS:
                    print(f"   - {level}: {len(dataset[level])}/{TARGET_PER_LEVEL}")
    except requests.RequestException as exc:
        print(f"❌ Erro de rede: {exc}. Aguardando para tentar novamente...")
        time.sleep(3)
        return 1
    except KeyboardInterrupt:
        print("\n🛑 Processo interrompido pelo usuário. Progresso salvo em palavras.json!")
        return 130

    print(f"🎉 Extração concluída com sucesso! Arquivo gerado em: {OUTPUT_PATH}")
    return 0


if __name__ == "__main__":
    sys.exit(main())
