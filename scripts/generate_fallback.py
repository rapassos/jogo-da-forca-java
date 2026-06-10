import requests
import json
import time
import re
import os
from urllib.parse import quote

# Configurações alinhadas com o Enum de Dificuldade do Java
LIMITS = {
    "facil": {"min": 3, "max": 5},
    "medio": {"min": 6, "max": 8},
    "dificil": {"min": 9, "max": 11},
    "extremo": {"min": 12, "max": 30}
}
TARGET_PER_LEVEL = 300
OUTPUT_PATH = "src/main/resources/palavras.json"

API_RANDOM = "https://api.dicionario-aberto.net/random"
API_WORD = "https://api.dicionario-aberto.net/word/"
HEADERS = {"User-Agent": "ForcaSwingApp/1.0 (Python Dataset Generator)"}

def clean_xml(raw_xml):
    """Remove as tags XML da definição, igualzinho ao código Java"""
    if not raw_xml:
        return "Significado indisponível."
    clean = re.sub(r'<[^>]*>', ' ', raw_xml)
    clean = re.sub(r'\s+', ' ', clean)
    return clean.strip()

def get_word_definition(word):
    """Busca o significado da palavra na API"""
    try:
        url = f"{API_WORD}{quote(word.lower())}"
        response = requests.get(url, headers=HEADERS, timeout=5)
        if response.status_code == 200:
            data = response.json()
            if isinstance(data, list) and len(data) > 0:
                xml_content = data[0].get("xml", "")
                return clean_xml(xml_content)
    except Exception:
        pass
    return "Significado indisponível."

def load_existing_data():
    """Carrega dados se o script já tiver sido rodado antes, evitando recomeçar do zero"""
    if os.path.exists(OUTPUT_PATH):
        try:
            with open(OUTPUT_PATH, "r", encoding="utf-8") as f:
                return json.load(f)
        except Exception:
            pass
    return {level: [] for level in LIMITS.keys()}

def main():
    dataset = load_existing_data()
    total_requests = 0
    
    print("🚀 Iniciando a extração de palavras da API Dicionário Aberto...")
    
    # Enquanto houver algum nível que não atingiu a meta de 300 palavras
    while any(len(dataset[lvl]) < TARGET_PER_LEVEL for lvl in LIMITS.keys()):
        try:
            # 1. Puxa uma palavra aleatória da API
            res = requests.get(API_RANDOM, headers=HEADERS, timeout=5)
            total_requests += 1
            
            if res.status_code != 200:
                print(f"⚠️ API retornou status {res.status_code}. Aguardando 5 segundos...")
                time.sleep(5)
                continue
                
            word = res.json().get("word", "").strip()
            word_lower = word.lower()
            length = len(word_lower)
            
            # Validação: ignora se tiver hífen, espaços ou caracteres inválidos
            if "-" in word_lower or " " in word_lower or not word_lower.isalpha():
                continue
                
            # 2. Descobre em qual nível a palavra se encaixa pelo tamanho
            matched_level = None
            for level, rules in LIMITS.items():
                if rules["min"] <= length <= rules["max"]:
                    matched_level = level
                    break
            
            if not matched_level:
                continue
                
            # 3. Se o nível ainda precisa de palavras e a palavra não for repetida
            current_list = dataset[matched_level]
            if len(current_list) < TARGET_PER_LEVEL and not any(item["word"] == word_lower for item in current_list):
                
                # Busca a definição completa
                definition = get_word_definition(word_lower)
                
                current_list.append({
                    "word": word_lower,
                    "definition": definition
                })
                
                print(f"✅ [{matched_level.upper()}] Adicionada: '{word_lower}' ({len(current_list)}/{TARGET_PER_LEVEL})")
                
                # Salva o arquivo de forma incremental a cada palavra nova encontrada
                with open(OUTPUT_PATH, "w", encoding="utf-8") as f:
                    json.dump(dataset, f, ensure_ascii=False, indent=2)
            
            # Pausa de segurança de 150ms para não estressar o servidor da API
            time.sleep(0.15)
            
            if total_requests % 100 == 0:
                print(f"📊 Status atual -> Requisições feitas: {total_requests}")
                for lvl in LIMITS.keys():
                    print(f"   - {lvl}: {len(dataset[lvl])}/{TARGET_PER_LEVEL}")
                    
        except requests.RequestException as e:
            print(f"❌ Erro de rede: {e}. Aguardando para tentar novamente...")
            time.sleep(3)
        except KeyboardInterrupt:
            print("\n🛑 Processo interrompido pelo usuário. Progresso salvo em palavras.json!")
            return

    print(f"🎉 Extração concluída com sucesso! Arquivo gerado em: {OUTPUT_PATH}")

if __name__ == "__main__":
    main()